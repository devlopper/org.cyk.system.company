package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.persistence.api.sale.SaleProductDao;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.business.api.chart.CartesianModelListener;
import org.cyk.system.root.business.api.chart.Series;
import org.cyk.system.root.business.api.chart.SeriesItem;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.time.Period;

public class SaleProductBusinessImpl extends AbstractTypedBusinessService<SaleProduct, SaleProductDao> implements SaleProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private LanguageBusiness languageBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	
	@Inject
	public SaleProductBusinessImpl(SaleProductDao dao) {
		super(dao);
	}
	
	@Override
	public Collection<SaleProduct> findBySales(Collection<Sale> sales) {
		return dao.readBySales(sales);
	}
	
	@Override
	public Collection<SaleProduct> findBySalesByCategories(Collection<Sale> sales,Collection<ProductCategory> productCategories) {
		Collection<SaleProduct> collection = findBySales(sales);
		if(productCategories==null || productCategories.isEmpty())
			return collection;
		Collection<SaleProduct> results = new ArrayList<>();
		for(SaleProduct saleProduct : collection){
			if(productCategories.contains(saleProduct.getSalableProduct().getProduct().getCategory()) || 
					productCategoryBusiness.isAtLeastOneAncestorOf(productCategories,saleProduct.getSalableProduct().getProduct().getCategory()))
				results.add(saleProduct);
		}
		return results;
	}

	@Override
	public void process(SaleProduct saleProduct) {
		logTrace("Processing {} ",saleProduct.getLogMessage());
		
		if(saleProduct.getSalableProduct().getPrice()==null){
		
		}else{
			saleProduct.getSalableProduct().setPrice(saleProduct.getSalableProduct().getPrice().multiply(saleProduct.getQuantity()).subtract(saleProduct.getReduction()));	
		}
		
		if(saleProduct.getSalableProduct().getPrice()==null){
			//logTrace("No price");
			return;
		}else{
			/*
			AccountingPeriod accountingPeriod = saleProduct.getSale().getAccountingPeriod();
			if(Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost())){
				BigDecimal divider = BigDecimal.ONE.add(accountingPeriod.getValueAddedTaxRate());
				saleProduct.setValueAddedTax(saleProduct.getPrice().divide(divider).subtract(saleProduct.getPrice()));
				saleProduct.setTurnover(saleProduct.getPrice().subtract(saleProduct.getValueAddedTax()));
			}else{
				saleProduct.setValueAddedTax(accountingPeriod.getValueAddedTaxRate().multiply(saleProduct.getPrice()));
				saleProduct.setTurnover(saleProduct.getPrice());
				//TODO price should be updated????
			}
			*/
			if(Boolean.TRUE.equals(saleProduct.getSale().getCompleted())){
				//logTrace("Before computing VAT. Current={} ,Auto compute={}",saleProduct.getValueAddedTax(),Boolean.TRUE.equals(saleProduct.getSale().getAutoComputeValueAddedTax()));
				//if(saleProduct.getValueAddedTax()==null)
				saleProduct.getCost().setValue(saleProduct.getSalableProduct().getPrice().add(saleProduct.getCommission()));
				if(Boolean.TRUE.equals(saleProduct.getSale().getAutoComputeValueAddedTax())){
					saleProduct.getCost().setTax(accountingPeriodBusiness.computeValueAddedTax(saleProduct.getSale().getAccountingPeriod(), saleProduct.getCost().getValue()));
				}else if(saleProduct.getCost().getTax()==null)
					saleProduct.getCost().setTax(BigDecimal.ZERO);
				saleProduct.getCost().setTurnover(accountingPeriodBusiness.computeTurnover(saleProduct.getSale().getAccountingPeriod()
						, saleProduct.getCost().getValue(),saleProduct.getCost().getTax()));
				//logIdentifiable("Completed",saleProduct);
			}else{
				if(saleProduct.getCost().getTax()==null){
					saleProduct.getCost().setTax(BigDecimal.ZERO);
					saleProduct.getCost().setTurnover(BigDecimal.ZERO);
				}
			}
			
			if(Boolean.TRUE.equals(saleProduct.getSale().getAccountingPeriod().getValueAddedTaxIncludedInCost())){
				
			}else{
				//TODO price should be updated????
				saleProduct.getCost().setValue(saleProduct.getCost().getValue().add(saleProduct.getCost().getTax()));			
				logTrace("Sale product {} price updated to {}",saleProduct.getSalableProduct().getProduct().getCode(),saleProduct.getCost().getValue());
			}
			
			//logDebug("Sale product {} data calculated | P={} VAT={} T={}",saleProduct.getProduct().getCode(),saleProduct.getPrice(),
			//		saleProduct.getValueAddedTax(),saleProduct.getTurnover());
			
			logIdentifiable("Processed",saleProduct);
		}
	}
	
	private CartesianModel salesCartesianModel(SalesResultsCartesianModelParameters parameters,CartesianModelListener<SaleProduct> cartesianModelListener,String nameId,String yAxisLabelId){
		if(parameters.getSaleProducts().isEmpty())
			return null;
		
		CartesianModel cartesianModel = new CartesianModel(languageBusiness.findText(nameId)+" - "+timeBusiness.formatPeriodFromTo(parameters.getPeriod()),
				parameters.getTimeDivisionType().getName(),
				languageBusiness.findText(yAxisLabelId));
		cartesianModel.getXAxis().setTickAngle(45);
		Series ySeries = cartesianModel.addSeries(languageBusiness.findText(nameId));
		
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

	@Override
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

	@Override
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
