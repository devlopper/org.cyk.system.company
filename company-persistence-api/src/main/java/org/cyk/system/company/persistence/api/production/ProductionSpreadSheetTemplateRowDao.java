package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetTemplateRowDao extends TypedDao<ProductionPlanResource> {

	Collection<ProductionPlanResource> readByProductionSpreadSheetTemplate(ProductionPlan productionSpreadSheetTemplate);

}
