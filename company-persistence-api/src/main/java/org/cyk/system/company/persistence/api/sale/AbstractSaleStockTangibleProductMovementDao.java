package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.AbstractSaleStockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractSaleStockTangibleProductMovementDao<SALE extends SaleStockTangibleProductMovement,SEARCH_CRITERIA extends AbstractSaleStockTangibleProductMovementSearchCriteria> extends TypedDao<SALE> {

	Collection<SALE> readByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE> readByStockTangibleProductStockMovements(Collection<StockTangibleProductMovement> tangibleProductStockMovements);
	SALE readByStockTangibleProductStockMovement(StockTangibleProductMovement tangibleProductStockMovement);

	SaleStocksDetails computeByCriteria(SEARCH_CRITERIA criteria);
}
