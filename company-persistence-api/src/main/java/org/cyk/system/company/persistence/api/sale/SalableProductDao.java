package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface SalableProductDao extends AbstractEnumerationDao<SalableProduct> {
	
	SalableProduct readByProduct(Product product);
	
}
