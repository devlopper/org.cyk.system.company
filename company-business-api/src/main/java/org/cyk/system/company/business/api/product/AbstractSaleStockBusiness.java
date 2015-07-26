package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;

public interface AbstractSaleStockBusiness<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractPeriodSearchCriteria> extends TypedBusiness<SALE_STOCK> {

	Collection<SALE_STOCK> findByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	
	Collection<SALE_STOCK> findByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements);
	SALE_STOCK findByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement);
	
}
