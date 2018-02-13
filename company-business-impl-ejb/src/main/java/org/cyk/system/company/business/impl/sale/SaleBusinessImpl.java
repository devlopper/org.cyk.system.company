package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.api.BusinessException;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.ConditionHelper;
import org.cyk.utility.common.helper.InstanceHelper;
import org.cyk.utility.common.helper.NumberHelper;

public class SaleBusinessImpl extends AbstractSaleBusinessImpl<Sale, SaleDao,Sale.SearchCriteria> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTION;
	}
	
	@Override
	public Sale instanciateOne() {
		Sale sale = super.instanciateOne();
		sale.setCascadeOperationToMaster(Boolean.TRUE);
    	sale.setCascadeOperationToMasterFieldNames(Arrays.asList(Sale.FIELD_SALABLE_PRODUCT_COLLECTION));
		sale.setSalableProductCollection(inject(SalableProductCollectionBusiness.class).instanciateOne().setIsProductQuantityUpdated(Boolean.TRUE));
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
	protected void afterCreate(Sale sale) {
		super.afterCreate(sale);
		if(sale.getBalanceMovementCollection() == null){
			sale.setBalanceMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.SALE_BALANCE
				,sale.getSalableProductCollection().getCost().getValue(),sale));
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(sale.getBalanceMovementCollection(), sale);
		}
	}
	
	@Override
	protected void beforeDelete(Sale sale) {
		sale.getSalableProductCollection().getItemsDeletable().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
		super.beforeDelete(sale);
	}
	
	/*@Override
	protected void setAutoSettedProperties(Sale sale, final Crud crud) {
		System.out.println("SaleBusinessImpl.setAutoSettedProperties() 000 : "+crud);
		new CollectionHelper.Iterator.Adapter.Default<SalableProductCollectionItem>(sale.getSalableProductCollection().getItems().getElements()){
			private static final long serialVersionUID = 1L;
			protected void __executeForEach__(SalableProductCollectionItem salableProductCollectionItem) {
				salableProductCollectionItem.setActionListener(new InstanceHelper.ActionListener.Adapter<SalableProductCollectionItem>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void actBefore(SalableProductCollectionItem salableProductCollectionItem, Constant.Action action) {
						if(salableProductCollectionItem.getSalableProduct().getProduct() instanceof TangibleProduct){
							System.out.println("SaleBusinessImpl.setAutoSettedProperties() "+action);
							TangibleProduct tangibleProduct = (TangibleProduct) salableProductCollectionItem.getSalableProduct().getProduct();
							SalableProductCollectionItem salableProductCollectionItemDB = Constant.Action.CREATE.equals(action) ? null : inject(SalableProductCollectionItemDao.class).read(salableProductCollectionItem.getIdentifier());
							if(Crud.DELETE.equals(crud))
								NumberHelper.getInstance().add(BigDecimal.class, tangibleProduct, TangibleProduct.FIELD_QUANTITY,salableProductCollectionItem.getQuantity());
							else
								NumberHelper.getInstance().add(BigDecimal.class, tangibleProduct, TangibleProduct.FIELD_QUANTITY
									, (BigDecimal)NumberHelper.getInstance()
									.subtract((salableProductCollectionItemDB == null ? BigDecimal.ZERO : salableProductCollectionItemDB.getQuantity()),salableProductCollectionItem.getQuantity()));	
							inject(TangibleProductBusiness.class).update(tangibleProduct);
						}
					}
					
				});
			}
		}.execute();
		super.setAutoSettedProperties(sale, crud);
	}*/
	
	/*@Override
	protected void afterCrud(Sale sale, final Crud crud) {
		super.afterCrud(sale, crud);
		Collection<SalableProductCollectionItem> salableProductCollectionItems = inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection());
		new CollectionHelper.Iterator.Adapter.Default<SalableProductCollectionItem>(salableProductCollectionItems){
			private static final long serialVersionUID = 1L;
			protected void __executeForEach__(SalableProductCollectionItem salableProductCollectionItem) {
				if(Crud.isCreateOrUpdate(crud)){
					if(salableProductCollectionItem.getSalableProduct().getProduct() instanceof TangibleProduct){
						TangibleProduct tangibleProduct = (TangibleProduct) salableProductCollectionItem.getSalableProduct().getProduct();
						if(Crud.CREATE.equals(crud))
							NumberHelper.getInstance().add(BigDecimal.class, tangibleProduct, TangibleProduct.FIELD_QUANTITY
									, NumberHelper.getInstance().negate(salableProductCollectionItem.getQuantity()));
						else if(Crud.DELETE.equals(crud))
							NumberHelper.getInstance().add(BigDecimal.class, tangibleProduct, TangibleProduct.FIELD_QUANTITY,salableProductCollectionItem.getQuantity());
						else{
							//System.out.println("SaleBusinessImpl.afterCrud() "+tangibleProduct.getQuantity()+" - "+salableProductCollectionItem.getQuantity());
							//NumberHelper.getInstance().add(BigDecimal.class, tangibleProduct, TangibleProduct.FIELD_QUANTITY
							//		, (BigDecimal)NumberHelper.getInstance().subtract(tangibleProduct.getQuantity(),salableProductCollectionItem.getQuantity()));
						}
						inject(TangibleProductBusiness.class).update(tangibleProduct);
					}
				}
			}
		}.execute();
	}*/
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleResults computeByCriteria(Sale.SearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}
		
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		return null;
	}
	
	@Override @Deprecated
	public void computeBalance(Sale sale) {
		//if( sale.getSalableProductCollection().isItemAggregationApplied())
		//	computeBalance(sale, inject(SaleCashRegisterMovementDao.class).sumAmountBySale(sale));
	}

	@Override @Deprecated
	public void computeBalance(Sale sale, Collection<SaleCashRegisterMovement> saleCashRegisterMovements) {
		BigDecimal sumOfSaleCashRegisterMovementAmount = BigDecimal.ZERO;
		if(saleCashRegisterMovements!=null)
			for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovements)
				sumOfSaleCashRegisterMovementAmount = sumOfSaleCashRegisterMovementAmount.add(saleCashRegisterMovement.getAmount());
		computeBalance(sale, sumOfSaleCashRegisterMovementAmount);
	}
	@Deprecated
	private void computeBalance(Sale sale,BigDecimal sumOfSaleCashRegisterMovementAmount){
		BigDecimal balanceValue = sale.getSalableProductCollection().getCost().getValue().subtract(sumOfSaleCashRegisterMovementAmount);
		
		throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("balance").setDomainNameIdentifier("sale")
			.setNumber1(balanceValue).setNumber2(BigDecimal.ZERO).setGreater(Boolean.FALSE).setEqual(Boolean.FALSE), BusinessException.class);
		
		//exceptionUtils().comparison(balanceValue.signum()==-1, "balance", ArithmeticOperator.GTE, BigDecimal.ZERO);
		Integer costValueBalanceValueComparison = sale.getSalableProductCollection().getCost().getValue().compareTo(balanceValue);
		/*
		exceptionUtils().comparison(costValueBalanceValueComparison==0 && !sale.getSalableProductCollection().getCost().getValue().equals(balanceValue), "field.cost : "+sale.getSalableProductCollection().getCost().getValue()
				, ArithmeticOperator.EQ, "field.balance : "+balanceValue);
		*/
		exceptionUtils().comparison(costValueBalanceValueComparison==-1, "balance", ArithmeticOperator.LTE, sale.getSalableProductCollection().getCost().getValue());
		//exceptionUtils().exception(costValueBalanceValueComparison==-1, "balancecannotbegreaterthancost");
		
		
		sale.getBalance().setValue(balanceValue);
		/*
		exceptionUtils().comparison(!Boolean.TRUE.equals(sale.getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getBalanceCanBeNegative()) 
				&& newBalance.signum() == -1, inject(LanguageBusiness.class).findText("field.balance"),ArithmeticOperator.GTE,BigDecimal.ZERO);
		*/
	}
		
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<Sale>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<Sale> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			/*
			@Override public void processOnConsume(Sale sale, Crud crud, Boolean first) {}
			
			@Override public Boolean isReportUpdatable(Sale sale) {
				return null;
			}
			
			@Override public void processOnReportUpdated(SaleReport saleReport,Boolean invoice) {}
			*/
			protected void cascade(Sale sale,Collection<SaleStockTangibleProductMovement> saleStockTangibleProductMovements,Collection<SaleCashRegisterMovement> saleCashRegisterMovements,Crud crud){
				new CascadeOperationListener.Adapter.Default<SaleStockTangibleProductMovement,SaleStockTangibleProductMovementDao,SaleStockTangibleProductMovementBusiness>(null,
						inject(SaleStockTangibleProductMovementBusiness.class))
				.operate(saleStockTangibleProductMovements, crud);
				new CascadeOperationListener.Adapter.Default<SaleCashRegisterMovement,SaleCashRegisterMovementDao,SaleCashRegisterMovementBusiness>(null,
						inject(SaleCashRegisterMovementBusiness.class))
				.operate(saleCashRegisterMovements, crud);
			}
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					@Override
					public void afterCreate(final Sale sale) {
						super.afterCreate(sale);
						cascade(sale,null,sale.getSaleCashRegisterMovements(), Crud.CREATE);
						
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
						sale.getBalance().setValue(sale.getSalableProductCollection().getCost().getValue());
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
