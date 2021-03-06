package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductStoreDao extends TypedDao<ProductStore> {
	
	ProductStore readByProductByStore(Product product,Store store);
	ProductStore readByProductCodeByStoreCode(String productCode,String storeCode);
	Collection<ProductStore> readByStore(Store store);
	
}
