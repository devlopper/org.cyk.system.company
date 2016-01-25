package org.cyk.system.company.business.api.stock;

import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockTangibleProductMovementBusiness extends TypedBusiness<StockTangibleProductMovement> {

	StockTangibleProductMovement instanciate(String[] arguments);
	List<StockTangibleProductMovement> instanciate(String[][] arguments);
	
	Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria);
	Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria);

}
