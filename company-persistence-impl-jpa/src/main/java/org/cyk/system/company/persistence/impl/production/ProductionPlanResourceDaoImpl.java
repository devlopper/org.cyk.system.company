package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.persistence.api.production.ProductionPlanResourceDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionPlanResourceDaoImpl extends AbstractTypedDao<ProductionPlanResource> implements ProductionPlanResourceDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionPlanModel;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionPlanModel, _select().where("template"));
	}
	
	@Override
	public Collection<ProductionPlanResource> readByProductionPlan(ProductionPlan productionPlanModel) {
		return namedQuery(readByProductionPlanModel).parameter("template", productionPlanModel).resultMany();
	}

	
	
}
