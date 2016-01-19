package org.cyk.system.company.persistence.impl.stock;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class StockableTangibleProductDaoImpl extends AbstractTypedDao<StockableTangibleProduct> implements StockableTangibleProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByTangibleProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByTangibleProduct, _select().where(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT));
	}
	
	@Override
	public StockableTangibleProduct readByTangibleProduct(TangibleProduct tangibleProduct) {
		return namedQuery(readByTangibleProduct).parameter(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT, tangibleProduct).ignoreThrowable(NoResultException.class).resultOne();
	}

	

}
