package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductGroupTypeBusiness;
import org.cyk.system.company.model.product.ProductGroupType;
import org.cyk.system.company.persistence.api.product.ProductGroupTypeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ProductGroupTypeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ProductGroupType,ProductGroupTypeDao> implements ProductGroupTypeBusiness {
	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public ProductGroupTypeBusinessImpl(ProductGroupTypeDao dao) {
        super(dao);
    }
	
}
