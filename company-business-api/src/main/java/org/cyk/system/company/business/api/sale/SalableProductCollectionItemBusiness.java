package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.api.AbstractCollectionItemBusiness;

public interface SalableProductCollectionItemBusiness extends AbstractCollectionItemBusiness<SalableProductCollectionItem,SalableProductCollection> {
    
	SalableProductCollectionItem instanciateOne(SalableProductCollection salableProductCollection,SalableProduct salableProduct,BigDecimal quantity
			,BigDecimal reduction,BigDecimal commission);
	
	SalableProductCollectionItem instanciateOne(SalableProductCollection salableProductCollection,String salableProductCode,String quantity
			,String reduction,String commission);
	
	
	void process(SalableProductCollectionItem salableProductCollectionItem);
	
	/*
	CartesianModel findCartesianModelTurnOver(SalesResultsCartesianModelParameters parameters);
	CartesianModel findCartesianModelCount(SalesResultsCartesianModelParameters parameters);
	
	Collection<SaleProduct> findBySales(Collection<Sale> sales);
	Collection<SaleProduct> findBySale(Sale identifiable);
	Collection<SaleProduct> findBySalesByCategories(Collection<Sale> sales,Collection<ProductCategory> productCategories);
	

	
	@Getter @Setter @AllArgsConstructor
	public static class SalesResultsCartesianModelParameters implements Serializable{

		private static final long serialVersionUID = -9100182801937100707L;
		
		private Period period;
		private Collection<SaleProduct> saleProducts;
		private Collection<ProductCategory> categories;
		private TimeDivisionType timeDivisionType;
	}
	*/
    
}
