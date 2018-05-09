package org.cyk.system.company.persistence.api.stock;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.persistence.api.TypedDao;

public interface StockableProductStoreDao extends TypedDao<StockableProductStore> {

	Collection<StockableProductStore> readByProductStores(Collection<ProductStore> productStores);
	Collection<StockableProductStore> readByProductStores(ProductStore...productStores);
	StockableProductStore readByProductStore(ProductStore productStore);
	
	Collection<StockableProductStore> readByStores(Collection<Store> stores);
	Collection<StockableProductStore> readByStores(Store...stores);
	Collection<StockableProductStore> readByStoreCodes(String...storeCodes);
	
}
