package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateColumnBusiness;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetTemplateColumnBusinessImpl extends AbstractTypedBusinessService<ProductionPlanMetric, ProductionPlanMetricDao> implements ProductionSpreadSheetTemplateColumnBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionSpreadSheetTemplateColumnBusinessImpl(ProductionPlanMetricDao dao) {
		super(dao);
	}

	
	
}
