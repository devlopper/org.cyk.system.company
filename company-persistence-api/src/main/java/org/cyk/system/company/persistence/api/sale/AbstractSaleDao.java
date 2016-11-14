package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractSaleDao<SALE extends AbstractSale,SEARCH_CRITERIA> extends TypedDao<SALE> {

	Collection<Sale> readByCriteria(SEARCH_CRITERIA criteria);
	
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	SALE readBySalableProductCollection(SalableProductCollection salableProductCollection);
}
