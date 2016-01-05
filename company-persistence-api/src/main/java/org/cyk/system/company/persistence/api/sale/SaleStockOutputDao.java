package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;

public interface SaleStockOutputDao extends AbstractSaleStockDao<SaleStockOutput,SaleStockOutputSearchCriteria> {

	Collection<SaleStockOutput> readBySaleStockInput(SaleStockInput saleStockInput);
	
}
