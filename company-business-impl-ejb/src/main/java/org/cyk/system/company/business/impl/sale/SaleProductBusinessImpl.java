package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleProductInstanceBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SaleProductDao;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.business.api.chart.CartesianModelListener;
import org.cyk.system.root.business.api.chart.Series;
import org.cyk.system.root.business.api.chart.SeriesItem;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.time.Period;

public class SaleProductBusinessImpl extends AbstractTypedBusinessService<SaleProduct, SaleProductDao> implements SaleProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleProductInstanceDao saleProductInstanceDao;
	
	@Inject
	public SaleProductBusinessImpl(SaleProductDao dao) {
		super(dao);
	}
	
	private void cascade(SaleProduct saleProduct,Collection<SaleProductInstance> saleProductInstances,Crud crud){
		new CascadeOperationListener.Adapter.Default<SaleProductInstance,SaleProductInstanceDao,SaleProductInstanceBusiness>(saleProductInstanceDao,CompanyBusinessLayer.getInstance().getSaleProductInstanceBusiness())
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
					CompanyBusinessLayer.getInstance().getProductCategoryBusiness().isAtLeastOneAncestorOf(productCategories,saleProduct.getSalableProduct().getProduct().getCategory()))
				results.add(saleProduct);
		}
		return results;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProduct> findBySale(Sale sale) {
		return findBySales(Arrays.asList(sale));
	}

	@Override
	public void process(SaleProduct saleProduct) {
		//logIdentifiable("Processing",saleProduct);
		if(saleProduct.getSalableProduct().getPrice()==null){
			/*
			 * This product has no unit price then the price to be paid must be specified by user
			 */
		}else{
			/*
			 * This product has a unit price so we can compute the cost to be paid
			 */
			BigDecimal cost = saleProduct.getSalableProduct().getPrice()
					.multiply(saleProduct.getQuantity())
					.subtract(saleProduct.getReduction())
					.add(saleProduct.getCommission());
			saleProduct.getCost().setValue(cost);
		}
		
		if(saleProduct.getCost().getValue()==null){
			
		}else{
			/*
			 * This product has a cost so we can compute the taxes to be paid
			 */
			if(Boolean.TRUE.equals(saleProduct.getSale().getAutoComputeValueAddedTax())){
				saleProduct.getCost().setTax(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(saleProduct.getSale().getAccountingPeriod(), saleProduct.getCost().getValue()));
			}else if(saleProduct.getCost().getTax()==null)
				saleProduct.getCost().setTax(BigDecimal.ZERO);
			saleProduct.getCost().setTurnover(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(saleProduct.getSale().getAccountingPeriod()
					, saleProduct.getCost().getValue(),saleProduct.getCost().getTax()));	
		}
		logIdentifiable("Processed",saleProduct);
	}
	
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
	
}
