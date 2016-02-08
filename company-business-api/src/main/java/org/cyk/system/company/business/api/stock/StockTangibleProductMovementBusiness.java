package org.cyk.system.company.business.api.stock;

import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockTangibleProductMovementBusiness extends TypedBusiness<StockTangibleProductMovement> {

	StockTangibleProductMovement instanciateOne(String[] arguments);
	List<StockTangibleProductMovement> instanciateMany(String[][] arguments);
	
	Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria);
	Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria);

}
