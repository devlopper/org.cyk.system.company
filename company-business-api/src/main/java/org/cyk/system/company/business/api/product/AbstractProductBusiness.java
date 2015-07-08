package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;

public interface AbstractProductBusiness<PRODUCT extends Product> extends AbstractEnumerationBusiness<PRODUCT> {

	PRODUCT create(PRODUCT product,OwnedCompany ownedCompany);
	/**
     * Compute and store data
     */
	void consume(Collection<SaleProduct> saleProducts);
	Collection<PRODUCT> findBySalable(Boolean salable);
	
	Collection<PRODUCT> findByCategory(ProductCategory category);
	
}
