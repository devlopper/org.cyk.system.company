package org.cyk.system.company.business.api.stock;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockableProductStoreBusiness extends TypedBusiness<StockableProductStore> {

	StockableProductStore findByProductStore(ProductStore productStore);
	
	void setQuantityMovementCollection(StockableProductStore stockableProductStore);
	
}
