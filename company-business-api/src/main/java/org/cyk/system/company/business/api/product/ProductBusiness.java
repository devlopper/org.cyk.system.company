package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;

public interface ProductBusiness extends AbstractProductBusiness<Product> {

	<T extends Product> Collection<T> findAll(Class<T> aClass);
	
	Collection<Product> findAllNot(Class<? extends Product> aClass);
	
}
