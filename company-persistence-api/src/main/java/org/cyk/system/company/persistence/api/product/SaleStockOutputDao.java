package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;

public interface SaleStockOutputDao extends AbstractSaleStockDao<SaleStockOutput,SaleStockOutputSearchCriteria> {

	Collection<SaleStockOutput> readBySaleStockInput(SaleStockInput saleStockInput);
	
}
