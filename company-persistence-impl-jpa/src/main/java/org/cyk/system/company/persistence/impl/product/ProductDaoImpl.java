package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.persistence.api.product.ProductDao;

public class ProductDaoImpl extends AbstractProductDaoImpl<Product> implements ProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final String FIELD_COLLECTION = "collection";
	
	private String readByCollection;
	
    @Override
    protected void namedQueriesInitialisation() {
        super.namedQueriesInitialisation();
        //registerNamedQuery(readByCollection, "SELECT product FROM Product product WHERE "
        //		+ "EXISTS(SELECT pCollection FROM ProductCollection pCollection WHERE pCollection = :collection AND product MEMBER OF pCollection.collection)");
        getConfiguration().setReadByClasses(Boolean.TRUE).setReadByNotClasses(Boolean.TRUE);
    }
    
	@Override
	public Collection<Product> readByCollection(ProductCollection collection) {
		return namedQuery(readByCollection).parameter(FIELD_COLLECTION, collection).resultMany();
	}
	
}
