package org.cyk.system.company.business.api.stock;

import java.util.Collection;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockTangibleProductMovementBusiness extends TypedBusiness<StockTangibleProductMovement> {

	Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria);
	Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria);

}
