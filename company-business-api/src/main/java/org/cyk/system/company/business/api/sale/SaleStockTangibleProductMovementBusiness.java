package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleStockTangibleProductMovementBusiness extends TypedBusiness<SaleStockTangibleProductMovement> {

	Collection<SaleStockTangibleProductMovement> findBySale(Sale sale);
	
}
