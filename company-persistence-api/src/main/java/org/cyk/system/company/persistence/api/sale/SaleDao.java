package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleDao extends TypedDao<Sale> {

	Sale readBySalableProductCollection(SalableProductCollection salableProductCollection);

}
