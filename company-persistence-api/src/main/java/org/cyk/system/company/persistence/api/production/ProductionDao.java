package org.cyk.system.company.persistence.api.production;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.model.spreadsheet.SpreadSheetSearchCriteria;
import org.cyk.system.root.persistence.api.spreadsheet.AbstractSpreadSheetDao;

public interface ProductionDao extends AbstractSpreadSheetDao<Production,ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionValue,BigDecimal,SpreadSheetSearchCriteria> {

	Collection<Production> readByProductionPlan(ProductionPlan productionPlan);

}
