package org.cyk.system.company.persistence.impl.sale;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.sale.AbstractSaleDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public abstract class AbstractSaleDaoImpl<SALE extends AbstractSale> extends AbstractTypedDao<SALE> implements AbstractSaleDao<SALE> {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySalableProductCollection;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readBySalableProductCollection,_select().where(AbstractSale.FIELD_SALABLE_PRODUCT_COLLECTION));
    }	
	
	/**/
	
	@Override
	public SALE readBySalableProductCollection(SalableProductCollection salableProductCollection) {
		return namedQuery(readBySalableProductCollection).parameter(Sale.FIELD_SALABLE_PRODUCT_COLLECTION, salableProductCollection)
				.ignoreThrowable(NoResultException.class).resultOne();
	}
	
}
