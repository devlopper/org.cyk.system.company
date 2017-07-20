package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.AbstractSaleBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.AbstractSaleDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.RootConstant;

public abstract class AbstractSaleBusinessImpl<SALE extends AbstractSale,DAO extends AbstractSaleDao<SALE,SEARCH_CRITERIA>,SEARCH_CRITERIA extends AbstractSale.SearchCriteria> extends AbstractTypedBusinessService<SALE, DAO> implements AbstractSaleBusiness<SALE,SEARCH_CRITERIA>,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public AbstractSaleBusinessImpl(DAO dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SALE instanciateOne() {
		SALE sale = super.instanciateOne();
		sale.setSalableProductCollection(new SalableProductCollection());
		sale.getSalableProductCollection().setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		sale.getSalableProductCollection().setAutoComputeValueAddedTax(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().signum()!=0);
		return sale;
	}
	
	@Override
	public SALE instanciateOne(String code,String name,Cost cost,String customerCode,Object[][] salableProducts) {
		SALE sale = instanciateOne();
		sale.setCode(code);
		sale.setName(name);
		sale.setCustomer(inject(CustomerDao.class).read(customerCode));
		sale.setSalableProductCollection(inject(SalableProductCollectionBusiness.class).instanciateOne(code,name,cost, salableProducts));
		sale.getSalableProductCollection().setAutoComputeValueAddedTax(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().signum()!=0);
		return sale;
	}
	
	@Override
	public SALE instanciateOne(String code,String customerCode,Object[][] salableProducts) {
		return instanciateOne(code,code,null,customerCode,salableProducts);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SALE instanciateOneRandomly(String code) {
		SALE sale = super.instanciateOneRandomly(code);
		sale.setSalableProductCollection(inject(SalableProductCollectionBusiness.class).instanciateOneRandomly(code));
		return sale;
	}
	
	@Override
	protected void afterCrud(SALE sale,Crud crud) {
		super.afterCrud(sale,crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(CompanyConstant.Configuration.Sale.AUTOMATICALLY_GENERATE_REPORT_FILE))
				createReportFile(sale, CompanyConstant.Code.ReportTemplate.INVOICE, RootConstant.Configuration.ReportTemplate.LOCALE);
		}
	}
	
	@Override
	protected void beforeCreate(SALE sale) {
		super.beforeCreate(sale);
		createIfNotIdentified(sale.getSalableProductCollection());
	}
	
	@Override
	protected void afterUpdate(SALE sale) {
		inject(SalableProductCollectionBusiness.class).update(sale.getSalableProductCollection());
		super.afterUpdate(sale);
	}
	
	@Override
	protected void afterDelete(SALE sale) {
		super.afterDelete(sale);
		if(inject(SalableProductCollectionBusiness.class).isIdentified(sale.getSalableProductCollection()))
			inject(SalableProductCollectionBusiness.class).delete(sale.getSalableProductCollection());
	}
	
	@Override
	public Collection<SALE> findByCriteria(SEARCH_CRITERIA criteria) {
		return null;
	}

	@Override
	public Long countByCriteria(SEARCH_CRITERIA criteria) {
		return null;
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
							if(StringUtils.isEmpty(sale.getCode()))
								sale.setCode(inject(StringGeneratorBusiness.class).generateIdentifier(sale,null,sale.getAccountingPeriod().getSaleConfiguration().getIdentifierGenerator()));
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
