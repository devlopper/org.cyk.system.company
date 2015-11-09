package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.root.business.api.spreadsheet.AbstractSpreadSheetTemplateBusiness;

public interface ProductionPlanBusiness extends AbstractSpreadSheetTemplateBusiness<ProductionPlan,ProductionPlanResource,ProductionPlanMetric> {

	Collection<ProductionPlan> findByProductionUnit(ProductionUnit identifiable);

	
	
}
