package org.cyk.system.company.persistence.impl.sale;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SalableDaoImpl extends AbstractTypedDao<SalableProduct> implements SalableProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduct, _select().where(SalableProduct.FIELD_PRODUCT));
	}
	
	@Override
	public SalableProduct readByProduct(Product product) {
		return namedQuery(readByProduct).parameter(SalableProduct.FIELD_PRODUCT, product).ignoreThrowable(NoResultException.class).resultOne();
	}

	

}
