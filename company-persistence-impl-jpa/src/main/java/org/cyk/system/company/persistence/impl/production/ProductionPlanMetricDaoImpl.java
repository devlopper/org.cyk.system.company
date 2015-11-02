package org.cyk.system.company.persistence.impl.production;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.root.persistence.impl.spreadsheet.AbstractSpreadSheetTemplateColumnDaoImpl;

public class ProductionPlanMetricDaoImpl extends AbstractSpreadSheetTemplateColumnDaoImpl<ProductionPlanMetric,ProductionPlanResource,ProductionPlan> implements ProductionPlanMetricDao {

	private static final long serialVersionUID = 6920278182318788380L;


}
