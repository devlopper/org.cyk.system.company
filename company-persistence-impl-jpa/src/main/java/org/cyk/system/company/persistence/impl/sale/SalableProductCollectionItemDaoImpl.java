package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;

public class SalableProductCollectionItemDaoImpl extends AbstractCollectionItemDaoImpl<SalableProductCollectionItem,SalableProductCollection> implements SalableProductCollectionItemDao,Serializable {

	private static final long serialVersionUID = 6306356272165070761L;

	private String readByCollectionBySalableProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCollectionBySalableProduct, _select().where(SalableProductCollectionItem.FIELD_COLLECTION)
				.and(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT));
	}
		
	@Override
	public Collection<SalableProductCollectionItem> readByCollectionBySalableProduct(SalableProductCollection salableProductCollection, SalableProduct salableProduct) {
		return namedQuery(readByCollectionBySalableProduct).parameter(SalableProductCollectionItem.FIELD_COLLECTION, salableProductCollection)
				.parameter(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT, salableProduct).resultMany();
	}
	
	
	
}
 