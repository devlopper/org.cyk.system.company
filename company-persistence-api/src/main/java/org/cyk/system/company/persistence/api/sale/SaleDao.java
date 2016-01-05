package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SalesDetails;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleDao extends TypedDao<Sale> {

	Collection<Sale> readByCriteria(SaleSearchCriteria criteria);
	
	Long countByCriteria(SaleSearchCriteria criteria);
	
	SalesDetails computeByCriteria(SaleSearchCriteria criteria);
	
	Sale readByComputedIdentifier(String identifier);
}
