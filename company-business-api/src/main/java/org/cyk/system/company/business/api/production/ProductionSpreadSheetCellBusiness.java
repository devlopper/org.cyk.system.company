package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductionSpreadSheetCellBusiness extends TypedBusiness<ProductionValue> {

	Collection<ProductionValue> findByProductionSpreadSheet(Production productionSpreadSheet);
	
}
