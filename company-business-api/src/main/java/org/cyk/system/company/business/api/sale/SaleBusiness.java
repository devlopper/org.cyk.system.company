package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleBusiness extends TypedBusiness<Sale> {
	
	SaleResults computeByCriteria(Sale.SearchCriteria criteria);
	
}
