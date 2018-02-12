package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
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
	
	/*
	@Override
	protected void afterCrud(SalableProductCollectionItem salableProductCollectionItem, Crud crud) {
		super.afterCrud(salableProductCollectionItem, crud);
		if(ArrayUtils.contains(new Crud[]{Crud.CREATE,Crud.UPDATE,Crud.DELETE}, crud)){
			if(Boolean.TRUE.equals(salableProductCollectionItem.getCascadeOperationToMaster()))
				cascadeUpdateCollectionCost(salableProductCollectionItem);
			
			if(Crud.isCreateOrUpdate(crud) && Boolean.TRUE.equals(salableProductCollectionItem.getCascadeOperationToChildren())){
				for(SaleCashRegisterMovement saleCashRegisterMovement : inject(SaleCashRegisterMovementDao.class).readBySale(inject(SaleDao.class)
						.read(salableProductCollectionItem.getCollection().getCode()))){
					inject(SaleCashRegisterMovementBusiness.class).update(saleCashRegisterMovement);
					//TODO do it well
					break;
				}
			}
		}
	}*/
		
	private void cascadeUpdateCollectionCost(SalableProductCollectionItem salableProductCollectionItem){
		//inject(SalableProductCollectionBusiness.class).computeCost(salableProductCollectionItem.getCollection());
		//inject(SalableProductCollectionDao.class).update(salableProductCollectionItem.getCollection());
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
		
	/*
	private void cascade(SaleProduct saleProduct,Collection<SaleProductInstance> saleProductInstances,Crud crud){
		new CascadeOperationListener.Adapter.Default<SaleProductInstance,SaleProductInstanceDao,SaleProductInstanceBusiness>(saleProductInstanceDao,inject(SaleProductInstanceBusiness.class))
			.operate(saleProductInstances, crud);
	}
	
	@Override
	public SaleProduct create(SaleProduct saleProduct) {
		saleProduct = super.create(saleProduct);
		cascade(saleProduct, saleProduct.getInstances(), Crud.CREATE);
		return saleProduct;
	}
	
	@Override
	public SaleProduct delete(SaleProduct saleProduct) {
		cascade(saleProduct, saleProductInstanceDao.readBySaleProduct(saleProduct), Crud.DELETE);
		return super.delete(saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProduct> findBySales(Collection<Sale> sales) {
		return dao.readBySales(sales);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<SaleProduct> findBySalesByCategories(Collection<Sale> sales,Collection<ProductCategory> productCategories) {
		Collection<SaleProduct> collection = findBySales(sales);
		if(productCategories==null || productCategories.isEmpty())
			return collection;
		Collection<SaleProduct> results = new ArrayList<>();
		for(SaleProduct saleProduct : collection){
			if(productCategories.contains(saleProduct.getSalableProduct().getProduct().getCategory()) || 
					inject(ProductCategoryBusiness.class).isAtLeastOneAncestorOf(productCategories,saleProduct.getSalableProduct().getProduct().getCategory()))
				results.add(saleProduct);
		}
		return results;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProduct> findBySale(Sale sale) {
		return findBySales(Arrays.asList(sale));
	}*/

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
}
