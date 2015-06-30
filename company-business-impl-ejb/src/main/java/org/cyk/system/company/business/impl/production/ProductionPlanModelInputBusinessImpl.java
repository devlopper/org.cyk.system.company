package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanModelInputBusiness;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelInputDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionPlanModelInputBusinessImpl extends AbstractTypedBusinessService<ProductionPlanModelInput, ProductionPlanModelInputDao> implements ProductionPlanModelInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionPlanModelInputBusinessImpl(ProductionPlanModelInputDao dao) {
		super(dao);
	}

	
	
}
