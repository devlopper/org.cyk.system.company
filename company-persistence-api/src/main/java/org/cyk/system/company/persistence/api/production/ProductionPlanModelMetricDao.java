package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionPlanModelMetricDao extends TypedDao<ProductionPlanModelMetric> {

	Collection<ProductionPlanModelMetric> readByProductionPlanModel(ProductionPlanModel productionPlanModel);

}
