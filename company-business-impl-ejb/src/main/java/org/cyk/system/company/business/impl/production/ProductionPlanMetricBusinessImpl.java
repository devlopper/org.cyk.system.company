package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanMetricBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.root.business.impl.spreadsheet.AbstractSpreadSheetTemplateColumnBusinessImpl;

public class ProductionPlanMetricBusinessImpl extends AbstractSpreadSheetTemplateColumnBusinessImpl<ProductionPlanMetric,ProductionPlanResource,ProductionPlan,ProductionPlanMetricDao> implements ProductionPlanMetricBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionPlanMetricBusinessImpl(ProductionPlanMetricDao dao) {
		super(dao);
	}

	
	
}
