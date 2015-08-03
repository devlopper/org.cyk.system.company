package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;

public interface ProductBusiness extends AbstractProductBusiness<Product> {

	Collection<Product> findByCollection(ProductCollection collection);
	
	Collection<Product> findToDelivery(Sale sale);

	Collection<Product> findNotCollectionBySale(Sale sale);
	
}
