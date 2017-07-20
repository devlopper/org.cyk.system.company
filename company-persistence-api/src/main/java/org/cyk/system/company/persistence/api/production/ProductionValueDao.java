package org.cyk.system.company.persistence.api.production;

import java.math.BigDecimal;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.persistence.api.spreadsheet.AbstractSpreadSheetCellDao;

public interface ProductionValueDao extends AbstractSpreadSheetCellDao<ProductionValue,ProductionPlanResource,ProductionPlanMetric,BigDecimal,ProductionPlan,Production> {

}
