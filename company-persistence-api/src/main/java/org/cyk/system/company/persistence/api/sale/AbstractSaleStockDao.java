package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.sale.SaleStock;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractSaleStockDao<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends TypedDao<SALE_STOCK> {

	Collection<SALE_STOCK> readByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE_STOCK> readByTangibleProductStockMovements(Collection<StockTangibleProductMovement> tangibleProductStockMovements);
	SALE_STOCK readByTangibleProductStockMovement(StockTangibleProductMovement tangibleProductStockMovement);

	SaleStocksDetails computeByCriteria(SEARCH_CRITERIA criteria);
}
