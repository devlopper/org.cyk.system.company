package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionPlanModelInputDao extends TypedDao<ProductionPlanModelInput> {

	Collection<ProductionPlanModelInput> readByProductionPlanModel(ProductionPlanModel productionPlanModel);

}
