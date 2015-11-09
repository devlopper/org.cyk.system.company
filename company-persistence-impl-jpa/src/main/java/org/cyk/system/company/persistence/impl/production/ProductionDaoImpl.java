package org.cyk.system.company.persistence.impl.production;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.persistence.api.production.ProductionDao;
import org.cyk.system.root.model.spreadsheet.SpreadSheetSearchCriteria;
import org.cyk.system.root.persistence.impl.spreadsheet.AbstractSpreadSheetDaoImpl;

public class ProductionDaoImpl extends AbstractSpreadSheetDaoImpl<Production,ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionValue,BigDecimal,SpreadSheetSearchCriteria> implements ProductionDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionPlan;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionPlan, _select().where(Production.FIELD_TEMPLATE,Production.FIELD_TEMPLATE));
	}

	@Override
	public Collection<Production> readByProductionPlan(ProductionPlan productionPlan) {
		return namedQuery(readByProductionPlan).parameter(Production.FIELD_TEMPLATE, productionPlan).resultMany();
	}
	
}
