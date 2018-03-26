package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.SalableProductStore;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;

public class SalableProductStoreCollectionItemBusinessImpl extends AbstractCollectionItemBusinessImpl<SalableProductStoreCollectionItem, SalableProductStoreCollectionItemDao,SalableProductStoreCollection> implements SalableProductStoreCollectionItemBusiness,Serializable {
	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public SalableProductStoreCollectionItemBusinessImpl(SalableProductStoreCollectionItemDao dao) {
		super(dao); 
	}
	
	@Override
	protected Object[] getPropertyValueTokens(SalableProductStoreCollectionItem salableProductStoreCollectionItem, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE,GlobalIdentifier.FIELD_NAME}, name))
			return new Object[]{salableProductStoreCollectionItem.getSalableProductStore()};
		return super.getPropertyValueTokens(salableProductStoreCollectionItem, name);
	}
				
	@Override
	protected void computeChanges(SalableProductStoreCollectionItem salableProductStoreCollectionItem, Builder logMessageBuilder) {
		super.computeChanges(salableProductStoreCollectionItem, logMessageBuilder);
		logMessageBuilder.addManyParameters("cost");
		logMessageBuilder.addNamedParameters("before",salableProductStoreCollectionItem.getCost().toString());
		if(salableProductStoreCollectionItem.getSalableProductStore()!=null && salableProductStoreCollectionItem.getSalableProductStore().getSalableProduct().getPrice()==null){
			//This product has no unit price then the price to be paid must be specified by user
			
		}else{
			//This product has a unit price so we can compute the cost to be paid
			BigDecimal cost = salableProductStoreCollectionItem.getQuantifiedPrice()
				//.subtract(salableProductStoreCollectionItem.getReduction())
				//.add(salableProductStoreCollectionItem.getCommission())
				;
			salableProductStoreCollectionItem.getCost().setValue(cost);
		}
		//TODO what if previous balance value has there ???
		salableProductStoreCollectionItem.getBalance().setValue(salableProductStoreCollectionItem.getCost().getValue());
		
		if(salableProductStoreCollectionItem.getCost().getValue()==null){
			
		}else{
			//This product has a cost so we can compute the taxes to be paid
			/*
			AccountingPeriod accountingPeriod = salableProductStoreCollectionItem.getCollection().getAccountingPeriod();
			if(Boolean.TRUE.equals(salableProductStoreCollectionItem.getCollection().getAutoComputeValueAddedTax())){
				salableProductStoreCollectionItem.getCost().setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(accountingPeriod, salableProductStoreCollectionItem.getCost().getValue()));
			}else if(salableProductStoreCollectionItem.getCost().getTax()==null)
				salableProductStoreCollectionItem.getCost().setTax(BigDecimal.ZERO);
			salableProductStoreCollectionItem.getCost().setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(accountingPeriod
					, salableProductStoreCollectionItem.getCost().getValue(),salableProductStoreCollectionItem.getCost().getTax()));
			*/
		}
		logMessageBuilder.addNamedParameters("after",salableProductStoreCollectionItem.getCost().toString());
	}
	
	@Override
	protected void beforeCrud(SalableProductStoreCollectionItem salableProductStoreCollectionItem, Crud crud) {
		super.beforeCrud(salableProductStoreCollectionItem, crud); 
		/*
		if(Boolean.TRUE.equals(salableProductStoreCollectionItem.getCollection().getIsBalanceMovementCollectionUpdatable())){			
			
		}		
		if(Boolean.TRUE.equals(salableProductStoreCollectionItem.getCollection().getIsStockMovementCollectionUpdatable())){
			StockableTangibleProduct stockableTangibleProduct = inject(StockableTangibleProductDao.class).readByTangibleProduct(
					(TangibleProduct) salableProductStoreCollectionItem.getSalableProductStore().getProduct());
			if(stockableTangibleProduct!=null){
				inject(MovementBusiness.class).create(stockableTangibleProduct, RootConstant.Code.MovementCollectionType.STOCK_REGISTER, crud, salableProductStoreCollectionItem
						, FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_COST,Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS),Boolean.TRUE,null);
			}
		}	
		*/
	}
		
}
