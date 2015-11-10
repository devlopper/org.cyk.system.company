package org.cyk.system.company.persistence.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.persistence.api.production.ResellerProductionPlanDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ResellerProductionPlanDaoImpl extends AbstractTypedDao<ResellerProductionPlan> implements ResellerProductionPlanDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByReseller,readByResellerByProductionPlan,readByProductionPlan;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByReseller, _select().where(ResellerProductionPlan.FIELD_RESELLER));
		registerNamedQuery(readByResellerByProductionPlan, _select().where(ResellerProductionPlan.FIELD_RESELLER).and(ResellerProductionPlan.FIELD_PRODUCTION_PLAN));
		registerNamedQuery(readByProductionPlan, _select().where(ResellerProductionPlan.FIELD_PRODUCTION_PLAN));
	}
	
	@Override
	public Collection<ResellerProductionPlan> readByReseller(Reseller reseller) {
		return namedQuery(readByReseller).parameter(ResellerProductionPlan.FIELD_RESELLER, reseller).resultMany();
	}

	@Override
	public ResellerProductionPlan readByResellerByProductionPlan(Reseller reseller,ProductionPlan manufacturedProduct) {
		return namedQuery(readByResellerByProductionPlan).parameter(ResellerProductionPlan.FIELD_RESELLER, reseller).parameter(ResellerProductionPlan.FIELD_PRODUCTION_PLAN, manufacturedProduct)
				.ignoreThrowable(NoResultException.class).resultOne();
	}

	@Override
	public Collection<ResellerProductionPlan> readByProductionPlan(ProductionPlan productionPlan) {
		return namedQuery(readByProductionPlan).parameter(ResellerProductionPlan.FIELD_PRODUCTION_PLAN, productionPlan).resultMany();
	}

}
