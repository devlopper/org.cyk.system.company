package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductProperties;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface SalableProductDao extends AbstractEnumerationDao<SalableProduct> {
	
	SalableProduct readByProduct(Product product);
	Collection<SalableProduct> readByProperties(SalableProductProperties salableProductProperties);
	Long countByProperties(SalableProductProperties salableProductProperties);
}
