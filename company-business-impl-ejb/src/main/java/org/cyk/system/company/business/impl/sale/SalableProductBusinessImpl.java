package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SalableProductBusinessImpl extends AbstractTypedBusinessService<SalableProduct, SalableProductDao> implements SalableProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}

}
