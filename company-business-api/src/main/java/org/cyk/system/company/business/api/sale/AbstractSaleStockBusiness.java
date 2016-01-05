package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.sale.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.sale.SaleStock;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.root.business.api.TypedBusiness;

public interface AbstractSaleStockBusiness<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends TypedBusiness<SALE_STOCK> {

	Collection<SALE_STOCK> findByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE_STOCK> findByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements);
	SALE_STOCK findByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement);
	
	SaleStocksDetails computeByCriteria(SEARCH_CRITERIA criteria);
	
}
