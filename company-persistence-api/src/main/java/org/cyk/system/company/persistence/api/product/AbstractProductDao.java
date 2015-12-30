package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface AbstractProductDao<PRODUCT extends Product> extends AbstractEnumerationDao<PRODUCT> {

	Collection<PRODUCT> readByCategory(ProductCategory category);
	
}
