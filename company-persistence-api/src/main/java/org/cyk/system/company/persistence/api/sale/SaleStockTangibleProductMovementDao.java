package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleStockTangibleProductMovementDao extends TypedDao<SaleStockTangibleProductMovement> {

	Collection<SaleStockTangibleProductMovement> readBySale(Sale sale);
	
}
