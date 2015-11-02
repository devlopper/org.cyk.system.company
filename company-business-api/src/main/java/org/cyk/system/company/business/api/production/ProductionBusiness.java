package org.cyk.system.company.business.api.production;

import java.math.BigDecimal;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.business.api.spreadsheet.AbstractSpreadSheetBusiness;
import org.cyk.system.root.model.spreadsheet.SpreadSheetSearchCriteria;

public interface ProductionBusiness extends AbstractSpreadSheetBusiness<Production,ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionValue,BigDecimal,SpreadSheetSearchCriteria> {

	
}
