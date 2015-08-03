package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;

public interface ProductDao extends AbstractProductDao<Product> {

	Collection<Product> readByCollection(ProductCollection collection);
	
}
