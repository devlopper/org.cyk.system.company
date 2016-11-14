package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleResults;

public interface SaleDao extends AbstractSaleDao<Sale,Sale.SearchCriteria> {

	SaleResults computeByCriteria(Sale.SearchCriteria criteria);
	
}
