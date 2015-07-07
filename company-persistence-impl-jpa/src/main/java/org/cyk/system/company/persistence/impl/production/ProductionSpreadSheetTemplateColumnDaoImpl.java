package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateColumnDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionSpreadSheetTemplateColumnDaoImpl extends AbstractTypedDao<ProductionSpreadSheetTemplateColumn> implements ProductionSpreadSheetTemplateColumnDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProductionPlanModel;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionPlanModel, _select().where("template"));
	}
	
	@Override
	public Collection<ProductionSpreadSheetTemplateColumn> readByProductionSpreadSheetTemplate(ProductionSpreadSheetTemplate productionPlanModel) {
		return namedQuery(readByProductionPlanModel).parameter("template", productionPlanModel).resultMany();
	}

	
	
}
