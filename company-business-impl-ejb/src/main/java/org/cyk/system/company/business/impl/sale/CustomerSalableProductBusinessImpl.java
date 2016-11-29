package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.CustomerSalableProductBusiness;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.persistence.api.sale.CustomerSalableProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class CustomerSalableProductBusinessImpl extends AbstractTypedBusinessService<CustomerSalableProduct, CustomerSalableProductDao> implements CustomerSalableProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CustomerSalableProductBusinessImpl(CustomerSalableProductDao dao) {
		super(dao);
	}
	
}
