package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.AbstractSaleStockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface AbstractSaleStockTangibleProductMovementBusiness<SALE_STOCK extends SaleStockTangibleProductMovement,SEARCH_CRITERIA extends AbstractSaleStockTangibleProductMovementSearchCriteria> extends TypedBusiness<SALE_STOCK> {

	Collection<SALE_STOCK> findByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE_STOCK> findByStockTangibleProductStockMovements(Collection<StockTangibleProductMovement> tangibleProductStockMovements);
	SALE_STOCK findByStockTangibleProductStockMovement(StockTangibleProductMovement tangibleProductStockMovement);
	
	SaleStocksDetails computeByCriteria(SEARCH_CRITERIA criteria);
	
}
