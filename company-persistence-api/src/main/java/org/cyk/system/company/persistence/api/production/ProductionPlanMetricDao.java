package org.cyk.system.company.persistence.api.production;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.root.persistence.api.spreadsheet.AbstractSpreadSheetTemplateColumnDao;

public interface ProductionPlanMetricDao extends AbstractSpreadSheetTemplateColumnDao<ProductionPlanMetric,ProductionPlanResource,ProductionPlan> {

}
