package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.LoggingHelper;
import org.cyk.utility.common.helper.StringHelper;

public class SaleBusinessImpl extends AbstractSaleBusinessImpl<Sale, SaleDao,Sale.SearchCriteria> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	/*
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTIONs;
	}*/
	
	@Override
	protected void computeChanges(Sale sale, LoggingHelper.Message.Builder logMessageBuilder) {
		super.computeChanges(sale, logMessageBuilder);
		if(sale.getBalanceMovementCollection()!=null){
			MovementCollection movementCollection = sale.getBalanceMovementCollection();
			if(isNotIdentified(sale))
				movementCollection.setValue(sale.getSalableProductCollection().getCost().getValue());
			if(StringHelper.getInstance().isBlank(movementCollection.getCode()) &&  StringHelper.getInstance().isNotBlank(sale.getCode()))
				movementCollection.setCode(RootConstant.Code.generate(sale.getCode(),movementCollection.getType().getCode()));
			if(StringHelper.getInstance().isBlank(movementCollection.getName()) && StringHelper.getInstance().isNotBlank(sale.getName()))
				movementCollection.setName(sale.getName()+Constant.CHARACTER_VERTICAL_BAR+movementCollection.getType().getName());
		}
	}
	
	@Override
	public Sale instanciateOne() {
		Sale sale = super.instanciateOne();
		sale.setCascadeOperationToMaster(Boolean.TRUE);
    	sale.setCascadeOperationToMasterFieldNames(Arrays.asList(Sale.FIELD_SALABLE_PRODUCT_COLLECTION));
		sale.setSalableProductCollection(inject(SalableProductCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE)
				.setIsBalanceMovementCollectionUpdatable(Boolean.TRUE));
		sale.setBalanceMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne().setTypeFromCode(RootConstant.Code.MovementCollectionType.SALE_BALANCE)
				.setValue(sale.getSalableProductCollection().getCost().getValue()));
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS) @Deprecated
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
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS) @Deprecated
	public List<Sale> instanciateMany(Object[][] arguments) {
		List<Sale> list = new ArrayList<>();
		for(Object[] argument : arguments)
			list.add(instanciateOne((String)argument[0], (String)argument[1], (String)argument[2], (String)argument[3], (String)argument[4]
					, (String[][])argument[5]));
		return list;
	}
	
	@Override
	protected void beforeCrud(Sale sale, Crud crud) {
		super.beforeCrud(sale, crud);
		if(Crud.CREATE.equals(crud)){
			
		}else {
			inject(MovementBusiness.class).create(sale, RootConstant.Code.MovementCollectionType.SALE_BALANCE, crud, sale
				, FieldHelper.getInstance().buildPath(Sale.FIELD_SALABLE_PRODUCT_COLLECTION,SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE),Boolean.FALSE,null);
		}
	}
	
	@Override
	protected void afterCreate(Sale sale) {
		super.afterCreate(sale);
		if(sale.getBalanceMovementCollection() != null){
			//sale.setBalanceMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.SALE_BALANCE
			//	,sale.getSalableProductCollection().getCost().getValue(),sale));
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(sale.getBalanceMovementCollection(), sale);
		}
	}
		
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleResults computeByCriteria(Sale.SearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}
		
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		return null;
	}
		
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<Sale>{
		
		Collection<Listener> COLLECTIONs = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<Sale> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					@Override
					public void afterCreate(final Sale sale) {
						super.afterCreate(sale);
						//cascade(sale,null,sale.getSaleCashRegisterMovements(), Crud.CREATE);
						
						if(sale.getAccountingPeriod()!=null){
							/*
							if(StringUtils.isEmpty(sale.getCode()))
								sale.setCode(inject(StringGeneratorBusiness.class).generateIdentifier(sale,null,sale.getAccountingPeriod().getSaleConfiguration().getIdentifierGenerator()));
							*/
							Cost cost = sale.getSalableProductCollection().getCost();
							if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
								cost.setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(sale.getAccountingPeriod(), cost.getValue()));
							cost.setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(sale.getAccountingPeriod(), cost.getValue(), cost.getTax()));	
						}
						sale.getSalableProductCollection().setCode(sale.getCode());
						//sale.getBalance().setValue(sale.getSalableProductCollection().getCost().getValue());
						/*if(Boolean.TRUE.equals(ListenerUtils.getInstance().getBoolean(Listener.COLLECTION, new ListenerUtils.BooleanMethod<Listener>() {
							@Override
							public Boolean execute(Listener listener) {
								return listener.isReportUpdatable(sale);
							}
							@Override
							public Boolean getNullValue() {
								return Boolean.TRUE;
							}
						}))){*/
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
						//}
						inject(SaleDao.class).update(sale);
					}
				}
			}
		}
		
	}

	
	
	
}
