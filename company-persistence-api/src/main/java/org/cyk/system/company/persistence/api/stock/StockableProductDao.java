package org.cyk.system.company.persistence.api.stock;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.root.persistence.api.TypedDao;

public interface StockableProductDao extends TypedDao<StockableProduct> {
	
	StockableProduct readByProduct(Product product);
	
}
