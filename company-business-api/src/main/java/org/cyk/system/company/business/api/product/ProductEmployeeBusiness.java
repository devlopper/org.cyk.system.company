package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductEmployeeBusiness extends TypedBusiness<ProductEmployee> {

	Collection<ProductEmployee> findPerformersBySale(Sale sale);
	
}
