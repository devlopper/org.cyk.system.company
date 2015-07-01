package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelInputDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionPlanModelInputDaoImpl extends AbstractTypedDao<ProductionPlanModelInput> implements ProductionPlanModelInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionPlanModel;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionPlanModel, _select().where("template"));
	}
	
	@Override
	public Collection<ProductionPlanModelInput> readByProductionPlanModel(ProductionPlanModel productionPlanModel) {
		return namedQuery(readByProductionPlanModel).parameter("template", productionPlanModel).resultMany();
	}

	
	
}
