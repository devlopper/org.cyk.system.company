package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SalesDetails;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleDao extends TypedDao<Sale> {

	Collection<Sale> readByCriteria(SaleSearchCriteria criteria);
	
	Long countByCriteria(SaleSearchCriteria criteria);

	SalesDetails computeByCriteria(SaleSearchCriteria criteria);
	
	Sale readByComputedIdentifier(String computedIdentifier);
	
	/**/
	
}
