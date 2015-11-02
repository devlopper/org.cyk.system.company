package org.cyk.system.company.persistence.impl.production;

import java.math.BigDecimal;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.persistence.api.production.ProductionValueDao;
import org.cyk.system.root.persistence.impl.spreadsheet.AbstractSpreadSheetCellDaoImpl;

public class ProductionValueDaoImpl extends AbstractSpreadSheetCellDaoImpl<ProductionValue,ProductionPlanResource,ProductionPlanMetric,BigDecimal,ProductionPlan,Production> implements ProductionValueDao {

	private static final long serialVersionUID = 6920278182318788380L;
	
	
}
