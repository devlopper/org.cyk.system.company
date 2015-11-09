package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.persistence.api.production.ProductionPlanDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionPlanDaoImpl extends AbstractTypedDao<ProductionPlan> implements ProductionPlanDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionUnit;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionUnit, _select().where(ProductionPlan.FIELD_PRODUCTION_UNIT));
	}
	
	@Override
	public Collection<ProductionPlan> readByProductionUnit(ProductionUnit productionUnit) {
		return namedQuery(readByProductionUnit).parameter(ProductionPlan.FIELD_PRODUCTION_UNIT, productionUnit).resultMany();
	}
	
}
