package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductSizeBusiness;
import org.cyk.system.company.model.product.ProductSize;
import org.cyk.system.company.persistence.api.product.ProductSizeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ProductSizeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ProductSize,ProductSizeDao> implements ProductSizeBusiness {
	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public ProductSizeBusinessImpl(ProductSizeDao dao) {
        super(dao);
    }
	
}
