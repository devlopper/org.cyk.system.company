package org.cyk.system.company.persistence.api.stock;

import java.util.Collection;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface StockTangibleProductMovementDao extends TypedDao<StockTangibleProductMovement> {

	Collection<StockTangibleProductMovement> readByCriteria(StockTangibleProductMovementSearchCriteria criteria);
    
	Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria);
	
}
