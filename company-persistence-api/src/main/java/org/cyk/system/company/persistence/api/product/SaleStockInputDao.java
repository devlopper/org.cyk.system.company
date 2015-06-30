package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleStockInputDao extends TypedDao<SaleStockInput> {

	Collection<SaleStockInput> readByCriteria(DefaultSearchCriteria criteria);
	Long countByCriteria(DefaultSearchCriteria criteria);
	
	Collection<SaleStockInput> readBySales(Collection<Sale> sales);
	SaleStockInput readBySale(Sale sale);
	
}
