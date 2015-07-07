package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetCellDao extends TypedDao<ProductionSpreadSheetCell> {

	Collection<ProductionSpreadSheetCell> readByProductionSpreadSheet(ProductionSpreadSheet productionSpreadSheet);

}
