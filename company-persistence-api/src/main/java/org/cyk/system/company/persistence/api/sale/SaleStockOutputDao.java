package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;

public interface SaleStockOutputDao extends AbstractSaleStockTangibleProductMovementDao<SaleStockTangibleProductMovementOutput,SaleStockOutputSearchCriteria> {

	Collection<SaleStockTangibleProductMovementOutput> readBySaleStockInput(SaleStockTangibleProductMovementInput saleStockInput);
	
}
