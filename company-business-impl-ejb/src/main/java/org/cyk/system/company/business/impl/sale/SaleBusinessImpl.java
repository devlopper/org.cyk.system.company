package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleProductDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineFinalStateDao;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.ListenerUtils;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public static Boolean AUTO_SET_SALE_DATE = Boolean.TRUE;
	
	@Inject private SaleProductDao saleProductDao;
	@Inject private SaleCashRegisterMovementDao saleCashRegisterMovementDao;
	@Inject private SaleStockTangibleProductMovementDao saleStockTangibleProductMovementDao;
	@Inject private SalableProductDao salableProductDao;
	@Inject private ProductDao productDao;
	@Inject private FiniteStateMachineFinalStateDao finiteStateMachineFinalStateDao;
	@Inject private PersonDao personDao;
	@Inject private CashierDao cashierDao;
	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne() {
		Sale sale = super.instanciateOne();
		sale.setAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent());
		sale.setAutoComputeValueAddedTax(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().signum()!=0);
		sale.setFiniteStateMachineState(sale.getAccountingPeriod().getSaleConfiguration().getFiniteStateMachine().getInitialState());
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne(Person person) {
		Sale sale = instanciateOne();
		sale.setCashier(CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson(person));
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Sale instanciateOne(String computedIdentifier,String cashierPersonCode, String customerRegistrationCode,String date,String taxable, String[][] salableProductInfos) {
		Sale sale = instanciateOne();
		sale.setComputedIdentifier(computedIdentifier);
		sale.setCashier(cashierPersonCode==null?cashierDao.select().one():cashierDao.readByPerson(personDao.readByCode(cashierPersonCode)));
		sale.setCustomer(customerRegistrationCode==null?null:customerDao.readByRegistrationCode(customerRegistrationCode));
		sale.setDate(StringUtils.isBlank(date) ? null : timeBusiness.parse(date));
		sale.setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
		for(String[] info : salableProductInfos){
			SaleProduct saleProduct =  selectProduct(sale, salableProductDao.readByProduct(productDao.read(info[0])), numberBusiness.parseBigDecimal(info[1]));
			if(info.length>2){
				saleProduct.getCost().setValue(numberBusiness.parseBigDecimal(info[2]));
				applyChange(sale, saleProduct);
			}
		}
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
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct,BigDecimal quantity) {
		SaleProduct saleProduct = new SaleProduct();
		saleProduct.setSale(sale);
		saleProduct.setSalableProduct(salableProduct);
		saleProduct.setQuantity(quantity);
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		sale.getSaleProducts().add(saleProduct);
		//if(saleProduct.getCost().getValue()!=null)
		//	sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_VALUE, saleProduct.getCost().getValue());
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE);
		logIdentifiable("Selected", saleProduct);
		return saleProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct) {
		return selectProduct(sale, salableProduct, BigDecimal.ONE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void unselectProduct(Sale sale, SaleProduct saleProduct) {
		if(sale.getSaleProducts() instanceof List<?>){
			List<SaleProduct> list = (List<SaleProduct>) sale.getSaleProducts();
			for(int i=0;i<list.size();){
				if(list.get(i)==saleProduct){
					list.remove(i);
					break;
				}else
					i++;
			}
		}
		//sale.getCost().setValue(sale.getCost().getValue().subtract(saleProduct.getCost().getValue()));
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_VALUE, saleProduct.getCost().getValue().negate());
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.negate());
		logIdentifiable("Unselected", saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void applyChange(Sale sale, SaleProduct saleProduct) {
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		updateCost(sale);
		logIdentifiable("Change applied", saleProduct);
	}
	
	private void updateCost(Sale sale){
		sale.getCost().setValue(BigDecimal.ZERO);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		}
	}
		
	@Override
	public Sale create(Sale sale) {
		SaleCashRegisterMovement saleCashRegisterMovement = sale.getSaleCashRegisterMovements().isEmpty() ? null : sale.getSaleCashRegisterMovements().iterator().next();
		exceptionUtils().exception(saleCashRegisterMovement!=null && finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())==null
				, "exception.sale.finitestatemachinestate.notfinal");
		//Firstly we create the sale
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		
		final Sale sale0 = sale;
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeCreate(sale0);
			}});
		
		sale = super.create(sale);
		cascade(sale, sale.getSaleProducts(),null,null, Crud.CREATE);
		consume(sale,Crud.CREATE);
		
		if(sale.getComputedIdentifier()==null)
			sale.setComputedIdentifier(generateIdentifier(sale,CompanyBusinessLayerListener.SALE_IDENTIFIER,sale.getAccountingPeriod().getSaleConfiguration()
				.getIdentifierGenerator()));
		sale = dao.update(sale);
		
		//Secondly we pay
		if(saleCashRegisterMovement==null){
			
		}else {
			//FIXME has be done to handled sale stock issue : 0 amount and X stock out. think another better way
			if(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO)){
				//logDebug("No sale cash register movement");
			}else if(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())!=null) {
				saleCashRegisterMovement.setSale(sale);
				CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().create(saleCashRegisterMovement);
				sale.getBalance().setCumul(sale.getBalance().getCumul().subtract(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));	 
			}
		}
		
		//Third we generate report
		final Sale sale1 = sale;
		Boolean updateReport = ListenerUtils.getInstance().getBoolean(Listener.COLLECTION, new ListenerUtils.BooleanMethod<Listener>() {
			@Override
			public Boolean execute(Listener listener) {
				return listener.isReportUpdatable(sale1);
			}
		});
		
		if(updateReport==null || Boolean.TRUE.equals(updateReport)){
			final SaleReport saleReport = CompanyBusinessLayer.getInstance().getCompanyReportProducer().produceSaleReport(sale);
			if(sale.getReport()==null)
				sale.setReport(new File());
			RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(sale, saleReport, sale.getAccountingPeriod().getSaleConfiguration().getSaleReportTemplate().getTemplate(), Boolean.TRUE);
			
			listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>() {
				@Override
				public void execute(Listener listener) {
					listener.processOnReportUpdated(saleReport, Boolean.TRUE);
				}
			});
			 
			dao.update(sale);
		}
		logIdentifiable("Created",sale);
		return sale;
	}
	
	private void cascade(Sale sale,Collection<SaleProduct> saleProducts,Collection<SaleStockTangibleProductMovement> saleStockTangibleProductMovements,Collection<SaleCashRegisterMovement> saleCashRegisterMovements,Crud crud){
		new CascadeOperationListener.Adapter.Default<SaleProduct,SaleProductDao,SaleProductBusiness>(saleProductDao,CompanyBusinessLayer.getInstance().getSaleProductBusiness())
			.operate(saleProducts, crud);
		new CascadeOperationListener.Adapter.Default<SaleStockTangibleProductMovement,SaleStockTangibleProductMovementDao,SaleStockTangibleProductMovementBusiness>(null,
				CompanyBusinessLayer.getInstance().getSaleStockTangibleProductMovementBusiness())
		.operate(saleStockTangibleProductMovements, crud);
		new CascadeOperationListener.Adapter.Default<SaleCashRegisterMovement,SaleCashRegisterMovementDao,SaleCashRegisterMovementBusiness>(null,
				CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness())
		.operate(saleCashRegisterMovements, crud);
	}
	
	@Override
	public void update(Sale sale,FiniteStateMachineAlphabet finiteStateMachineAlphabet) {
		exceptionUtils().exception(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())!=null, "exception.sale_state_cannotbeupdated");
		sale.setFiniteStateMachineState(RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness().findByFromStateByAlphabet(sale.getFiniteStateMachineState(), finiteStateMachineAlphabet));
		consume(sale,Crud.UPDATE);
		dao.update(sale);
	}
	
	private void consume(Sale sale,Crud crud){
		if(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())==null){
			
		}else{
			/*
			 * It is a final state so we can compute the derived values
			 */
			
			if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
				sale.getCost().setTax(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(sale.getAccountingPeriod(), sale.getCost().getValue()));
			sale.getCost().setTurnover(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(sale.getAccountingPeriod(), sale.getCost().getValue(), sale.getCost().getTax()));
			sale.getBalance().setValue(sale.getCost().getValue());
			
			commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_VALUE
					,Boolean.TRUE.equals(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxIncludedInCost()) ? BigDecimal.ZERO:sale.getCost().getTax());
			
			for(Listener listener : Listener.COLLECTION)
				listener.processOnConsume(sale, crud, Boolean.TRUE);
			
			logIdentifiable("Derived values computed",sale);
		}
	}
	
	@Override
	public Sale delete(Sale sale) {
		for(Listener listener : Listener.COLLECTION)
			listener.processOnConsume(sale, Crud.DELETE, Boolean.FALSE);
		cascade(sale, saleProductDao.readBySale(sale),saleStockTangibleProductMovementDao.readBySale(sale),saleCashRegisterMovementDao.readBySale(sale), Crud.DELETE);
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
		return RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(sale.getReport()
				,CompanyBusinessLayer.getInstance().getPointOfSaleInvoiceReportName()+Constant.CHARACTER_UNDESCORE+StringUtils.defaultString(sale.getComputedIdentifier(),sale.getIdentifier().toString()));
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
