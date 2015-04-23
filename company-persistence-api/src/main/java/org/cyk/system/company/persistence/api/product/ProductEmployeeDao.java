package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductEmployeeDao extends TypedDao<ProductEmployee> {

	Collection<ProductEmployee> readPerformersBySale(Sale sale);

}
 