package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;

public interface ProductBusiness extends AbstractEnumerationBusiness<Product> {

	void setProviderParty(Product product);
	Collection<Product> findByCategory(ProductCategory category);
		
}
