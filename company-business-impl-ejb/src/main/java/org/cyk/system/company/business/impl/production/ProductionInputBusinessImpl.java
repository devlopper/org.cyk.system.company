package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionInputBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.persistence.api.production.ProductionInputDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionInputBusinessImpl extends AbstractTypedBusinessService<ProductionInput, ProductionInputDao> implements ProductionInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionInputBusinessImpl(ProductionInputDao dao) {
		super(dao);
	}

	@Override
	public Collection<ProductionInput> findByProduction(Production production) {
		return dao.readByProduction(production);
	}

	
	
}
