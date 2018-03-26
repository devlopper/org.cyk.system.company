package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductStoreTypeBusiness;
import org.cyk.system.company.model.product.ProductStoreType;
import org.cyk.system.company.persistence.api.product.ProductStoreTypeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ProductStoreTypeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ProductStoreType,ProductStoreTypeDao> implements ProductStoreTypeBusiness {
	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public ProductStoreTypeBusinessImpl(ProductStoreTypeDao dao) {
        super(dao);
    }

}
