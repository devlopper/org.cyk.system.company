package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.movement.MovementBusiness;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.LoggingHelper;
import org.cyk.utility.common.helper.StringHelper;

public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

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
		if(sale.getSalableProductStoreCollection()!=null)
			inject(SalableProductStoreCollectionBusiness.class).computeChanges(sale.getSalableProductStoreCollection());
		if(sale.getBalanceMovementCollection()!=null){
			MovementCollection movementCollection = sale.getBalanceMovementCollection();
			if(isNotIdentified(sale))
				movementCollection.setValue(sale.getSalableProductStoreCollection().getCost().getValue());
			if(StringHelper.getInstance().isBlank(movementCollection.getCode()) &&  StringHelper.getInstance().isNotBlank(sale.getCode()))
				movementCollection.setCode(RootConstant.Code.generate(sale.getCode(),movementCollection.getType().getCode()));
			if(StringHelper.getInstance().isBlank(movementCollection.getName()) && StringHelper.getInstance().isNotBlank(sale.getName()))
				movementCollection.setName(sale.getName()+Constant.CHARACTER_VERTICAL_BAR+movementCollection.getType().getName());
		}
	}
	
	@Override
	public Sale instanciateOne() {
		Sale sale = super.instanciateOne();
		sale.setSalableProductStoreCollection(inject(SalableProductStoreCollectionBusiness.class).instanciateOne().setIsBalanceMovementCollectionUpdatable(Boolean.TRUE)
				.setIsStockMovementCollectionUpdatable(Boolean.TRUE));
		sale.setBalanceMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne().setTypeFromCode(RootConstant.Code.MovementCollectionType.SALE_BALANCE)
				.setValue(sale.getSalableProductStoreCollection().getCost().getValue()));		
    	sale.addCascadeOperationToMasterFieldNames(Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION);
		return sale;
	}
	
	@Override
	public Collection<String> findRelatedInstanceFieldNames(Sale sale) {
		return Arrays.asList(Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION);
	}
	
	@Override
	protected void beforeCrud(Sale sale, Crud crud) {
		super.beforeCrud(sale, crud);
		if(Crud.CREATE.equals(crud)){
			
		}else {
			inject(MovementBusiness.class).create(sale, RootConstant.Code.MovementCollectionType.SALE_BALANCE, crud, sale
				, FieldHelper.getInstance().buildPath(Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION,SalableProductStoreCollection.FIELD_COST,Cost.FIELD_VALUE),Boolean.FALSE,null);
		}
	}
	
	@Override
	protected void afterCrud(Sale sale, Crud crud) {
		super.afterCrud(sale, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Crud.CREATE.equals(crud)){
				if(sale.getBalanceMovementCollection() != null){
					//sale.setBalanceMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.SALE_BALANCE
					//	,sale.getSalableProductCollection().getCost().getValue(),sale));
					inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(sale.getBalanceMovementCollection(), sale);
				}
			}
		}else if(Crud.DELETE.equals(crud)){
			
		}
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
						
						//if(sale.getAccountingPeriod()!=null){
							/*
							if(StringUtils.isEmpty(sale.getCode()))
								sale.setCode(inject(StringGeneratorBusiness.class).generateIdentifier(sale,null,sale.getAccountingPeriod().getSaleConfiguration().getIdentifierGenerator()));
							*/
							/*
							Cost cost = sale.getSalableProductCollection().getCost();
							if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
								cost.setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(sale.getAccountingPeriod(), cost.getValue()));
							cost.setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(sale.getAccountingPeriod(), cost.getValue(), cost.getTax()));
							*/
						//}
						sale.getSalableProductStoreCollection().setCode(sale.getCode());
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
