package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionPlanMetricDaoImpl extends AbstractTypedDao<ProductionPlanMetric> implements ProductionPlanMetricDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionPlan;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionPlan, _select().where("template"));
	}
	
	@Override
	public Collection<ProductionPlanMetric> readByProductionPlan(ProductionPlan productionPlan) {
		return namedQuery(readByProductionPlan).parameter("template", productionPlan).resultMany();
	}

	
	
}
