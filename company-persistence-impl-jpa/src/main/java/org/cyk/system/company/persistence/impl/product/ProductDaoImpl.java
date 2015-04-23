package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.persistence.api.product.ProductDao;

public class ProductDaoImpl extends AbstractProductDaoImpl<Product> implements ProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByClass,readByNotClass,readByCollection;
	
    @Override
    protected void namedQueriesInitialisation() {
        super.namedQueriesInitialisation();
        registerNamedQuery(readByClass, "SELECT product FROM Product product WHERE TYPE(product) = :aClass");
        registerNamedQuery(readByNotClass, "SELECT product FROM Product product WHERE TYPE(product) <> :aClass");
        registerNamedQuery(readByCollection, "SELECT product FROM Product product WHERE "
        		+ "EXISTS(SELECT pCollection FROM ProductCollection pCollection WHERE pCollection = :collection AND product MEMBER OF pCollection.collection)");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Product> Collection<T> readAll(Class<T> aClass) {
        return (Collection<T>) namedQuery(readByClass).parameter("aClass", aClass).resultMany();
    }
    
    @Override
    public Collection<Product> readAllNot(Class<? extends Product> aClass) {
    	return namedQuery(readByNotClass).parameter("aClass", aClass).resultMany();
    }

	@Override
	public Collection<Product> readByCollection(ProductCollection collection) {
		return namedQuery(readByCollection).parameter("collection", collection).resultMany();
	}
	
}
