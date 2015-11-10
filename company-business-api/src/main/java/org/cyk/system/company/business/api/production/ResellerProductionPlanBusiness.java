package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ResellerProductionPlanBusiness extends TypedBusiness<ResellerProductionPlan> {

	Collection<ResellerProductionPlan> findByReseller(Reseller reseller);
	Collection<ResellerProductionPlan> findByProductionPlan(ProductionPlan productionPlan);
	
}
