package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanResourceBusiness;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.persistence.api.production.ProductionPlanResourceDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionPlanResourceBusinessImpl extends AbstractTypedBusinessService<ProductionPlanResource, ProductionPlanResourceDao> implements ProductionPlanResourceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionPlanResourceBusinessImpl(ProductionPlanResourceDao dao) {
		super(dao);
	}

	
	
}
