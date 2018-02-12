package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.persistence.api.product.ProductDao;

public class ProductBusinessImpl extends AbstractProductBusinessImpl<Product,ProductDao> implements ProductBusiness,Serializable  {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ProductBusinessImpl(ProductDao dao) {
        super(dao);
    }
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Product> findByCollection(ProductCollection collection) {
		return dao.readByCollection(collection);
	}
	
}
