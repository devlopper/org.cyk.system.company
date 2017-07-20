package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductCollection;

public interface ProductCollectionBusiness extends AbstractProductBusiness<ProductCollection> {

	Collection<ProductCollection> findAllWithProduct();
	
}
