package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionPlanMetricDao extends TypedDao<ProductionPlanMetric> {

	Collection<ProductionPlanMetric> readByProductionPlan(ProductionPlan productionPlan);

}
