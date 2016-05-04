package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;
import org.cyk.system.root.business.api.Crud;

public interface AbstractProductBusiness<PRODUCT extends Product> extends AbstractEnumerationBusiness<PRODUCT> {

	PRODUCT create(PRODUCT product,OwnedCompany ownedCompany);

	void consume(Sale sale, Crud crud, Boolean first);
	
	Collection<PRODUCT> findByCategory(ProductCategory category);
	
}
