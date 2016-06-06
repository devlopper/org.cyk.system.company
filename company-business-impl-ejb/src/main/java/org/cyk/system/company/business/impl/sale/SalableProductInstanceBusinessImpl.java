package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;

public class SalableProductInstanceBusinessImpl extends AbstractCollectionItemBusinessImpl<SalableProductInstance, SalableProductInstanceDao,SalableProduct> implements SalableProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceBusinessImpl(SalableProductInstanceDao dao) {
		super(dao);
	}
	
	@Override
	public SalableProductInstance create(SalableProductInstance salableProductInstance) {
		if(StringUtils.isBlank(salableProductInstance.getName()))
			salableProductInstance.setName(salableProductInstance.getCollection().getName());
		return super.create(salableProductInstance);
	}

}
