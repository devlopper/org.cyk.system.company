package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;

public interface SaleDao extends AbstractSaleDao<Sale> {

	Collection<Sale> readByCriteria(SaleSearchCriteria criteria);
	
	Long countByCriteria(SaleSearchCriteria criteria);
	
	SaleResults computeByCriteria(SaleSearchCriteria criteria);
	
}
