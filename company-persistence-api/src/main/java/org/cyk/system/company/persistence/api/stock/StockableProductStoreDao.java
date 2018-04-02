package org.cyk.system.company.persistence.api.stock;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.persistence.api.TypedDao;

public interface StockableProductStoreDao extends TypedDao<StockableProductStore> {

	StockableProductStore readByProductStore(ProductStore productStore);
	
}
