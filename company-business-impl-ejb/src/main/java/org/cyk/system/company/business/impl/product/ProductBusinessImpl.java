package org.cyk.system.company.business.impl.product;

import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.persistence.api.product.ProductDao;

public class ProductBusinessImpl extends AbstractProductBusinessImpl<Product,ProductDao> implements ProductBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ProductBusinessImpl(ProductDao dao) {
        super(dao);
    }

	@Override
	public <T extends Product> Collection<T> findAll(Class<T> aClass) {
		return dao.readAll(aClass);
	}

	@Override
	public Collection<Product> findAllNot(Class<? extends Product> aClass) {
		return dao.readAllNot(aClass);
	}
 
}
