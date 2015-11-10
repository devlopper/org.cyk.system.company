package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ResellerProductionPlanDao extends TypedDao<ResellerProductionPlan> {

	ResellerProductionPlan readByResellerByProductionPlan(Reseller reseller,ProductionPlan productionPlan);
	Collection<ResellerProductionPlan> readByReseller(Reseller reseller);
	Collection<ResellerProductionPlan> readByProductionPlan(ProductionPlan productionPlan);

}
