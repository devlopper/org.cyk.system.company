package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.utility.common.ListenerUtils;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne() {
		Sale sale = super.instanciateOne();
		sale.setSalableProductCollection(new SalableProductCollection());
		sale.getSalableProductCollection().setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		sale.getSalableProductCollection().setAutoComputeValueAddedTax(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().signum()!=0);
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne(Person person) {
		Sale sale = instanciateOne();
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne(String computedIdentifier,String cashierPersonCode, String customerRegistrationCode,String date,String taxable, String[][] salableProductInfos) {
		Sale sale = instanciateOne();
		sale.setCode(computedIdentifier);
		//sale.setCashier(cashierPersonCode==null?cashierDao.select().one():cashierDao.readByPerson(personDao.readByCode(cashierPersonCode)));
		sale.setCustomer(customerRegistrationCode==null?null:customerDao.read(customerRegistrationCode));
		sale.setBirthDate(StringUtils.isBlank(date) ? null : timeBusiness.parse(date));
		sale.getSalableProductCollection().setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
		/*for(String[] info : salableProductInfos){
			SalableProductCollectionItem saleProduct =  selectProduct(sale, salableProductDao.readByProduct(productDao.read(info[0])), numberBusiness.parseBigDecimal(info[1]));
			if(info.length>2){
				saleProduct.getCost().setValue(numberBusiness.parseBigDecimal(info[2]));
				applyChange(sale, saleProduct);
			}
		}*/
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Sale> instanciateMany(Object[][] arguments) {
		List<Sale> list = new ArrayList<>();
		for(Object[] argument : arguments)
			list.add(instanciateOne((String)argument[0], (String)argument[1], (String)argument[2], (String)argument[3], (String)argument[4]
					, (String[][])argument[5]));
		return list;
	}
			
	@Override
	public Sale create(final Sale sale) {
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeCreate(sale);
			}});
		if(sale.getBirthDate()==null)
			sale.setBirthDate(universalTimeCoordinated());
		super.create(sale);
		
		cascade(sale,null,sale.getSaleCashRegisterMovements(), Crud.CREATE);
		
		if(sale.getAccountingPeriod()!=null){
			if(sale.getCode()==null)
				sale.setCode(generateIdentifier(sale,CompanyBusinessLayer.Listener.SALE_IDENTIFIER,sale.getAccountingPeriod().getSaleConfiguration().getIdentifierGenerator()));
			
			Cost cost = sale.getSalableProductCollection().getCost();
			if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
				cost.setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(sale.getAccountingPeriod(), cost.getValue()));
			cost.setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(sale.getAccountingPeriod(), cost.getValue(), cost.getTax()));	
		}
		sale.getBalance().setValue(sale.getSalableProductCollection().getCost().getValue());
		/*
		if(sale.getSaleCashRegisterMovements()!=null)
			for(SaleCashRegisterMovement saleCashRegisterMovement : sale.getSaleCashRegisterMovements()){
				saleCashRegisterMovement.setSale(sale);
				inject(SaleCashRegisterMovementBusiness.class).create(saleCashRegisterMovement);
				commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_CUMUL
						, saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate());	 
			}
		*/	
		if(Boolean.TRUE.equals(ListenerUtils.getInstance().getBoolean(Listener.COLLECTION, new ListenerUtils.BooleanMethod<Listener>() {
			@Override
			public Boolean execute(Listener listener) {
				return listener.isReportUpdatable(sale);
			}
			@Override
			public Boolean getNullValue() {
				return Boolean.TRUE;
			}
		}))){
			/*
			final SaleReport saleReport = inject(CompanyReportProducer.class).produceSaleReport(sale);
			if(sale.getReport()==null)
				sale.setReporù8t(new File());
			inject(ReportBusiness.class).buildBinaryContent(sale, saleReport, sale.getAccountingPeriod().getSaleConfiguration().getSaleReportTemplate().getTemplate(), Boolean.TRUE);
			
			listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>() {
				@Override
				public void execute(Listener listener) {
					listener.processOnReportUpdated(saleReport, Boolean.TRUE);
				}
			});
			*/
		}
		dao.update(sale);
		
		logIdentifiable("Created",sale);
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.afterCreate(sale);
			}});
		return sale;
	}
	
	private void cascade(Sale sale,Collection<SaleStockTangibleProductMovement> saleStockTangibleProductMovements,Collection<SaleCashRegisterMovement> saleCashRegisterMovements,Crud crud){
		new CascadeOperationListener.Adapter.Default<SaleStockTangibleProductMovement,SaleStockTangibleProductMovementDao,SaleStockTangibleProductMovementBusiness>(null,
				inject(SaleStockTangibleProductMovementBusiness.class))
		.operate(saleStockTangibleProductMovements, crud);
		new CascadeOperationListener.Adapter.Default<SaleCashRegisterMovement,SaleCashRegisterMovementDao,SaleCashRegisterMovementBusiness>(null,
				inject(SaleCashRegisterMovementBusiness.class))
		.operate(saleCashRegisterMovements, crud);
	}
	
	@Override
	public void update(Sale sale,FiniteStateMachineAlphabet finiteStateMachineAlphabet) {
		consume(sale,Crud.UPDATE);
		dao.update(sale);
	}
	
	private void consume(Sale sale,Crud crud){
		/*
		
		
		commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_VALUE
				,Boolean.TRUE.equals(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxIncludedInCost()) ? BigDecimal.ZERO:sale.getCost().getTax());
		
		for(Listener listener : Listener.COLLECTION)
			listener.processOnConsume(sale, crud, Boolean.TRUE);
		*/
		logIdentifiable("Derived values computed",sale);
		
	}
	
	@Override
	public Sale delete(Sale sale) {
		/*for(Listener listener : Listener.COLLECTION)
			listener.processOnConsume(sale, Crud.DELETE, Boolean.FALSE);
		cascade(sale, saleProductDao.readBySale(sale),saleStockTangibleProductMovementDao.readBySale(sale),saleCashRegisterMovementDao.readBySale(sale), Crud.DELETE);
		*/
		return super.delete(sale);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Sale> findByCriteria(SaleSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SaleSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleResults computeByCriteria(SaleSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}
		
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		/*
		return RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(sale.getReport()
				,CompanyBusinessLayer.getInstance().getPointOfSaleInvoiceReportName()+Constant.CHARACTER_UNDESCORE+StringUtils.defaultString(sale.getComputedIdentifier(),sale.getIdentifier().toString()));
		*/
		return null;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Sale findByComputedIdentifier(String identifier) {
		return dao.readByComputedIdentifier(identifier);
	}
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<Sale>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/

		void processOnConsume(Sale sale,Crud crud, Boolean first);
		
		Boolean isReportUpdatable(Sale sale);
		
		void processOnReportUpdated(SaleReport saleReport,Boolean invoice);
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<Sale> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			@Override public void processOnConsume(Sale sale, Crud crud, Boolean first) {}
			
			@Override public Boolean isReportUpdatable(Sale sale) {
				return null;
			}
			
			@Override public void processOnReportUpdated(SaleReport saleReport,Boolean invoice) {}
			
		}
		
	}
	
}
