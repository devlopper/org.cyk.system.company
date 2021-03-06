package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

public class ProductDaoImpl extends AbstractEnumerationDaoImpl<Product> implements ProductDao {
	private static final long serialVersionUID = 6920278182318788380L;

	protected String readByCategory;
	
    @Override
    protected void namedQueriesInitialisation() {
        super.namedQueriesInitialisation();
        registerNamedQuery(readByCategory, _select().where(Product.FIELD_CATEGORY));
    }
    
    @Override
	public Collection<Product> readByCategory(ProductCategory category) {
		return namedQuery(readByCategory).parameter(Product.FIELD_CATEGORY, category).resultMany();
	}
	
}
