package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateRow;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetTemplateRowDao extends TypedDao<ProductionSpreadSheetTemplateRow> {

	Collection<ProductionSpreadSheetTemplateRow> readByProductionSpreadSheetTemplate(ProductionSpreadSheetTemplate productionSpreadSheetTemplate);

}
