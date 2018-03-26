package org.cyk.system.company.business.api.stock;

import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockableProductStoreBusiness extends TypedBusiness<StockableProductStore> {

	void setQuantityMovementCollection(StockableProductStore stockableProductStore);
	
}
