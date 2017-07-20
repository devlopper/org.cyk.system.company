package org.cyk.system.company.business.api.production;

import java.math.BigDecimal;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.business.api.spreadsheet.AbstractSpreadSheetCellBusiness;

public interface ProductionSpreadSheetCellBusiness extends AbstractSpreadSheetCellBusiness<ProductionValue,ProductionPlanResource,ProductionPlanMetric,BigDecimal,ProductionPlan,Production> {

	
}
