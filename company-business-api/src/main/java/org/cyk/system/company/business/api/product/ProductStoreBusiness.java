package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.Store;

public interface ProductStoreBusiness extends TypedBusiness<ProductStore> {

	ProductStore findByProductByStore(Product product,Store store);
	
}
