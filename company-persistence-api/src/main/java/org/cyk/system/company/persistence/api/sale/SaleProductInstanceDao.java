package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleProductInstanceDao extends TypedDao<SaleProductInstance> {

	Collection<SaleProductInstance> readBySaleProduct(SaleProduct saleProduct);

}
