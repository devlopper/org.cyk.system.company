package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetCellBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.persistence.api.production.ProductionValueDao;
import org.cyk.system.root.business.impl.spreadsheet.AbstractSpreadSheetCellBusinessImpl;

public class ProductionSpreadSheetCellBusinessImpl extends AbstractSpreadSheetCellBusinessImpl<ProductionValue,ProductionPlanResource,ProductionPlanMetric,BigDecimal,ProductionPlan,Production, ProductionValueDao> implements ProductionSpreadSheetCellBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionSpreadSheetCellBusinessImpl(ProductionValueDao dao) {
		super(dao);
	}
	
	
}
