package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface TangibleProductStockMovementBusiness extends TypedBusiness<TangibleProductStockMovement> {

	void create(Collection<TangibleProductStockMovement> movements);

}
