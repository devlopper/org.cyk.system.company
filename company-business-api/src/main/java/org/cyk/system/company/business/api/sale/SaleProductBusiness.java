package org.cyk.system.company.business.api.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.model.time.TimeDivisionType;

public interface SaleProductBusiness extends TypedBusiness<SaleProduct> {

	BigDecimal computeCost(SaleProduct saleProduct,Boolean taxIncluded);
	void process(SaleProduct saleProduct);
	
	CartesianModel findCartesianModelTurnOver(SalesResultsCartesianModelParameters parameters);
	CartesianModel findCartesianModelCount(SalesResultsCartesianModelParameters parameters);
	
	Collection<SaleProduct> findBySales(Collection<Sale> sales);
	Collection<SaleProduct> findBySalesByCategories(Collection<Sale> sales,Collection<ProductCategory> productCategories);
	
	/**/
	
	@Getter @Setter @AllArgsConstructor
	public static class SalesResultsCartesianModelParameters implements Serializable{

		private static final long serialVersionUID = -9100182801937100707L;
		
		private Period period;
		private Collection<SaleProduct> saleProducts;
		private Collection<ProductCategory> categories;
		private TimeDivisionType timeDivisionType;
	}
}
