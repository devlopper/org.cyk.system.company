package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleProductDao extends TypedDao<SaleProduct> {

	Collection<SaleProduct> readBySale(Sale sale);

	Collection<SaleProduct> readBySales(Collection<Sale> sales);

}
