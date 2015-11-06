package org.cyk.system.company.persistence.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.company.persistence.api.production.ResellerProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ResellerProductDaoImpl extends AbstractTypedDao<ResellerProduct> implements ResellerProductDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByReseller,readByProduct,readByResellerByProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByReseller, _select().where(ResellerProduct.FIELD_RESELLER));
		registerNamedQuery(readByProduct, _select().where(ResellerProduct.FIELD_PRODUCT));
		registerNamedQuery(readByResellerByProduct, _select().where(ResellerProduct.FIELD_RESELLER).and(ResellerProduct.FIELD_PRODUCT));
	}
	
	@Override
	public Collection<ResellerProduct> readByReseller(Reseller reseller) {
		return namedQuery(readByReseller).parameter(ResellerProduct.FIELD_RESELLER, reseller).resultMany();
	}

	@Override
	public Collection<ResellerProduct> readByProduct(Product product) {
		return namedQuery(readByProduct).parameter(ResellerProduct.FIELD_PRODUCT, product).resultMany();
	}

	@Override
	public ResellerProduct readByResellerByProduct(Reseller reseller,Product product) {
		return namedQuery(readByResellerByProduct).parameter(ResellerProduct.FIELD_RESELLER, reseller).parameter(ResellerProduct.FIELD_PRODUCT, product)
				.ignoreThrowable(NoResultException.class).resultOne();
	}

}
