package org.cyk.system.company.persistence.impl.product;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.persistence.api.product.ProductCollectionDao;

public class ProductCollectionDaoImpl extends AbstractProductDaoImpl<ProductCollection> implements ProductCollectionDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readAllWithProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		//registerNamedQuery(readAllWithProduct, "SELECT DISTINCT pc FROM ProductCollection pc LEFT JOIN FETCH pc.collection");
	}
	
	@Override
	public Collection<ProductCollection> readAllWithProduct() {
		return namedQuery(readAllWithProduct).resultMany();
	}

}
