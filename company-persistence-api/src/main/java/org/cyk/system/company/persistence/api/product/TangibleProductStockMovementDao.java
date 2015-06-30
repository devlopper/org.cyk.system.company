package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.product.TangibleProductStockMovementSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface TangibleProductStockMovementDao extends TypedDao<TangibleProductStockMovement> {

	Collection<TangibleProductStockMovement> readByCriteria(TangibleProductStockMovementSearchCriteria criteria);
    
	Long countByCriteria(TangibleProductStockMovementSearchCriteria criteria);
	
}
