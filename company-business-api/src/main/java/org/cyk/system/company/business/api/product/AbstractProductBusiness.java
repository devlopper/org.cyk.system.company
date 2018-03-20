package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;

public interface AbstractProductBusiness<PRODUCT extends Product> extends AbstractEnumerationBusiness<PRODUCT> {

	void setProviderParty(PRODUCT product);
	Collection<PRODUCT> findByCategory(ProductCategory category);
	
}
