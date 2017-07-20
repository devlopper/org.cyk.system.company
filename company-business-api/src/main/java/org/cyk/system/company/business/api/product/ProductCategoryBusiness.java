package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.pattern.tree.AbstractDataTreeTypeBusiness;

public interface ProductCategoryBusiness extends AbstractDataTreeTypeBusiness<ProductCategory> {

	ProductCategory create(ProductCategory productCategory,OwnedCompany ownedCompany);
	
}
