package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleStockOutputDao extends TypedDao<SaleStockOutput> {

	Collection<SaleStockOutput> readByCriteria(DefaultSearchCriteria criteria);
	Long countByCriteria(DefaultSearchCriteria criteria);
	Collection<SaleStockOutput> readBySaleStockInput(SaleStockInput saleStockInput);
	
}
