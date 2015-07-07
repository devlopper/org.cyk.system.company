package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductionSpreadSheetCellBusiness extends TypedBusiness<ProductionSpreadSheetCell> {

	Collection<ProductionSpreadSheetCell> findByProductionSpreadSheet(ProductionSpreadSheet productionSpreadSheet);
	
}
