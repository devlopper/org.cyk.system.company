package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductGroupBusiness;
import org.cyk.system.company.model.product.ProductGroup;
import org.cyk.system.company.model.product.ProductGroupType;
import org.cyk.system.company.persistence.api.product.ProductGroupDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeBusinessImpl;

public class ProductGroupBusinessImpl extends AbstractDataTreeBusinessImpl<ProductGroup,ProductGroupDao,ProductGroupType> implements ProductGroupBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ProductGroupBusinessImpl(ProductGroupDao dao) {
        super(dao);
    }
 
}
