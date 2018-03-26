package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;

public class ProductStoreBusinessImpl extends AbstractTypedBusinessService<ProductStore, ProductStoreDao> implements ProductStoreBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ProductStoreBusinessImpl(ProductStoreDao dao) {
		super(dao);
	}
	
	
	
}
