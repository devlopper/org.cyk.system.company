package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;

public class SalableProductCollectionItemBusinessImpl extends AbstractCollectionItemBusinessImpl<SalableProductCollectionItem, SalableProductCollectionItemDao,SalableProductCollection> implements SalableProductCollectionItemBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public SalableProductCollectionItemBusinessImpl(SalableProductCollectionItemDao dao) {
		super(dao); 
	}
	
	@Override
	protected Object[] getPropertyValueTokens(SalableProductCollectionItem salableProductCollectionItem, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE,GlobalIdentifier.FIELD_NAME}, name))
			return new Object[]{salableProductCollectionItem.getSalableProduct()};
		return super.getPropertyValueTokens(salableProductCollectionItem, name);
	}
			
	@Override @Deprecated
	public SalableProductCollectionItem instanciateOne(SalableProductCollection salableProductCollection,
			SalableProduct salableProduct, BigDecimal quantity, BigDecimal reduction, BigDecimal commission) {
		SalableProductCollectionItem salableProductCollectionItem = instanciateOne(salableProductCollection,salableProduct.getCode(),salableProduct.getName(),Boolean.FALSE);
		//salableProductCollectionItem.setCode(salableProduct.getCode());
		//salableProductCollectionItem.setCollection(salableProductCollection);
		salableProductCollectionItem.setSalableProduct(salableProduct);
		salableProductCollectionItem.setQuantity(quantity);
		salableProductCollectionItem.setReduction(reduction);
		salableProductCollectionItem.setCommission(commission);
		computeChanges(salableProductCollectionItem);
		inject(SalableProductCollectionBusiness.class).add(salableProductCollection, salableProductCollectionItem);
		return salableProductCollectionItem;
	}

	@Override @Deprecated
	public SalableProductCollectionItem instanciateOne(SalableProductCollection salableProductCollection,
			String salableProductCode, String quantity, String reduction, String commission) {
		return instanciateOne(salableProductCollection, inject(SalableProductDao.class).read(salableProductCode), commonUtils.getBigDecimal(quantity)
				, commonUtils.getBigDecimal(reduction), commonUtils.getBigDecimal(commission));
	}
	
	@Override @Deprecated
	public SalableProductCollectionItem instanciateOne(String salableProductCollectionCode,Object[] salableProduct) {
		return instanciateOne(inject(SalableProductCollectionDao.class).read(salableProductCollectionCode), inject(SalableProductDao.class).read((String)salableProduct[0])
				, commonUtils.getBigDecimal(commonUtils.getValueAt(salableProduct, 1, "0").toString())
				, commonUtils.getBigDecimal(commonUtils.getValueAt(salableProduct, 2, "0").toString())
				, commonUtils.getBigDecimal(commonUtils.getValueAt(salableProduct, 3, "0").toString()));
	}
		
	@Override
	protected void computeChanges(SalableProductCollectionItem salableProductCollectionItem, Builder logMessageBuilder) {
		super.computeChanges(salableProductCollectionItem, logMessageBuilder);
		logMessageBuilder.addManyParameters("cost");
		logMessageBuilder.addNamedParameters("before",salableProductCollectionItem.getCost().toString());
		if(salableProductCollectionItem.getSalableProduct().getPrice()==null){
			//This product has no unit price then the price to be paid must be specified by user
			
		}else{
			//This product has a unit price so we can compute the cost to be paid
			salableProductCollectionItem.setQuantifiedPrice(salableProductCollectionItem.getSalableProduct().getPrice()
					.multiply(salableProductCollectionItem.getQuantity())); 
			BigDecimal cost = salableProductCollectionItem.getQuantifiedPrice()
				.subtract(salableProductCollectionItem.getReduction())
				.add(salableProductCollectionItem.getCommission());
			salableProductCollectionItem.getCost().setValue(cost);
		}
		//TODO what if previous balance value has there ???
		salableProductCollectionItem.getBalance().setValue(salableProductCollectionItem.getCost().getValue());
		salableProductCollectionItem.getCost().setNumberOfProceedElements(salableProductCollectionItem.getQuantity());
		
		if(salableProductCollectionItem.getCost().getValue()==null){
			
		}else{
			//This product has a cost so we can compute the taxes to be paid
			AccountingPeriod accountingPeriod = salableProductCollectionItem.getCollection().getAccountingPeriod();
			if(Boolean.TRUE.equals(salableProductCollectionItem.getCollection().getAutoComputeValueAddedTax())){
				salableProductCollectionItem.getCost().setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(accountingPeriod, salableProductCollectionItem.getCost().getValue()));
			}else if(salableProductCollectionItem.getCost().getTax()==null)
				salableProductCollectionItem.getCost().setTax(BigDecimal.ZERO);
			salableProductCollectionItem.getCost().setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(accountingPeriod
					, salableProductCollectionItem.getCost().getValue(),salableProductCollectionItem.getCost().getTax()));	
		}
		logMessageBuilder.addNamedParameters("after",salableProductCollectionItem.getCost().toString());
	}
	
	@Override
	protected void beforeCrud(SalableProductCollectionItem salableProductCollectionItem, Crud crud) {
		super.beforeCrud(salableProductCollectionItem, crud); 
		if(Boolean.TRUE.equals(salableProductCollectionItem.getCollection().getIsBalanceMovementCollectionUpdatable())){			
			
		}		
		if(Boolean.TRUE.equals(salableProductCollectionItem.getCollection().getIsStockMovementCollectionUpdatable())){
			StockableTangibleProduct stockableTangibleProduct = inject(StockableTangibleProductDao.class).readByTangibleProduct(
					(TangibleProduct) salableProductCollectionItem.getSalableProduct().getProduct());
			if(stockableTangibleProduct!=null){
				inject(MovementBusiness.class).create(stockableTangibleProduct, RootConstant.Code.MovementCollectionType.STOCK_REGISTER, crud, salableProductCollectionItem
						, SalableProductCollectionItem.FIELD_QUANTITY,Boolean.TRUE,null);
			}
		}		
	}
	
	/*
	private CartesianModel salesCartesianModel(SalesResultsCartesianModelParameters parameters,CartesianModelListener<SaleProduct> cartesianModelListener,String nameId,String yAxisLabelId){
		if(parameters.getSaleProducts().isEmpty())
			return null;
		
		CartesianModel cartesianModel = new CartesianModel(inject(LanguageBusiness.class).findText(nameId)+" - "+timeBusiness.formatPeriodFromTo(parameters.getPeriod()),
				parameters.getTimeDivisionType().getName(),
				inject(LanguageBusiness.class).findText(yAxisLabelId));
		cartesianModel.getXAxis().setTickAngle(45);
		Series ySeries = cartesianModel.addSeries(inject(LanguageBusiness.class).findText(nameId));
		
		for(Period period : timeBusiness.findPeriods(parameters.getPeriod(), parameters.getTimeDivisionType(),Boolean.TRUE)){
			String x = timeBusiness.formatPeriod(period, parameters.getTimeDivisionType());
			BigDecimal y = BigDecimal.ZERO;
			for(SaleProduct saleProduct : parameters.getSaleProducts())
				if(timeBusiness.between(period, saleProduct.getSale().getDate(), Boolean.FALSE, Boolean.FALSE))
					y = y.add(cartesianModelListener.y(saleProduct));
				
			if(!Boolean.TRUE.equals(cartesianModelListener.skipY(y)))
				ySeries.getItems().add(new SeriesItem(x, y));
			
		}
		return cartesianModel;
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public CartesianModel findCartesianModelTurnOver(SalesResultsCartesianModelParameters parameters) {
		return salesCartesianModel(parameters,new CartesianModelListener<SaleProduct>() {
			@Override
			public BigDecimal y(SaleProduct saleProduct) {
				return saleProduct.getCost().getTurnover();
			} 
			
			@Override
			public Boolean skipY(BigDecimal y) {
				return Boolean.FALSE;
			}
			
		},"field.turnover","amount");
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public CartesianModel findCartesianModelCount(SalesResultsCartesianModelParameters parameters) {
		return salesCartesianModel(parameters,new CartesianModelListener<SaleProduct>() {
			@Override
			public BigDecimal y(SaleProduct saleProduct) {
				return saleProduct.getQuantity();
			} 
			
			@Override
			public Boolean skipY(BigDecimal y) {
				return Boolean.FALSE;
			}
			
		},"field.number.of.sales","field.quantity");
	}
	*/	
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<SalableProductCollectionItem>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<SalableProductCollectionItem> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					@Override
					public void afterCreate(final SalableProductCollectionItem salableProductCollectionItem) {
						super.afterCreate(salableProductCollectionItem);
						
					}
				}
			}
		}
	}
}
