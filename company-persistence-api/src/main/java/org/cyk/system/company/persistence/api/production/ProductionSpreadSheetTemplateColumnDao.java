package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetTemplateColumnDao extends TypedDao<ProductionSpreadSheetTemplateColumn> {

	Collection<ProductionSpreadSheetTemplateColumn> readByProductionSpreadSheetTemplate(ProductionPlan productionPlanModel);

}
