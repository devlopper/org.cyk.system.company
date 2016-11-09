package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractSaleDao<SALE extends AbstractSale> extends TypedDao<SALE> {

	SALE readBySalableProductCollection(SalableProductCollection salableProductCollection);
}
