package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;

public interface ProductDao extends AbstractProductDao<Product> {

	<T extends Product> Collection<T> readAll(Class<T> aClass);
	
	Collection<Product> readAllNot(Class<? extends Product> aClass);

	Collection<Product> readByCollection(ProductCollection collection);
	
}
