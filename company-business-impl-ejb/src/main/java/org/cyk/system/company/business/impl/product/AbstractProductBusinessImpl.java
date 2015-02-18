package org.cyk.system.company.business.impl.product;

import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT> {
	
	private static final long serialVersionUID = 2801588592108008404L;

    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
 
}
