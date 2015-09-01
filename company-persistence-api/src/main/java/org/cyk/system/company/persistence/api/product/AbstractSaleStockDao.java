package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStocksDetails;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractSaleStockDao<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends TypedDao<SALE_STOCK> {

	Collection<SALE_STOCK> readByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE_STOCK> readByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements);
	SALE_STOCK readByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement);

	SaleStocksDetails computeByCriteria(SEARCH_CRITERIA criteria);
}
