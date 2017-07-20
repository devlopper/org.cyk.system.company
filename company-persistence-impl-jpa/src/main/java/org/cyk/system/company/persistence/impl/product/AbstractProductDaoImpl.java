package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

public abstract class AbstractProductDaoImpl<PRODUCT extends Product> extends AbstractEnumerationDaoImpl<PRODUCT> implements AbstractProductDao<PRODUCT> {

	private static final long serialVersionUID = 6920278182318788380L;

	protected String readByCategory;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCategory, _select().where(Product.FIELD_CATEGORY));
	}
	
	@Override
	public Collection<PRODUCT> readByCategory(ProductCategory category) {
		return namedQuery(readByCategory).parameter(Product.FIELD_CATEGORY, category).resultMany();
	}
	
}
