package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanModelMetricBusiness;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelMetricDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionPlanModelMetricBusinessImpl extends AbstractTypedBusinessService<ProductionPlanModelMetric, ProductionPlanModelMetricDao> implements ProductionPlanModelMetricBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionPlanModelMetricBusinessImpl(ProductionPlanModelMetricDao dao) {
		super(dao);
	}

	
	
}
