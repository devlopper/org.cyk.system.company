package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionItemDao;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;

public class SalableProductStoreCollectionItemDaoImpl extends AbstractCollectionItemDaoImpl<SalableProductStoreCollectionItem,SalableProductStoreCollection> implements SalableProductStoreCollectionItemDao,Serializable {

	private static final long serialVersionUID = 6306356272165070761L;

	private String readByCollectionBySalableProductStore,sumCostAttributesBySalableProductStoreCollection;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCollectionBySalableProductStore, _select().where(SalableProductStoreCollectionItem.FIELD_COLLECTION)
				.and(SalableProductStoreCollectionItem.FIELD_SALABLE_PRODUCT_STORE));
		registerNamedQuery(sumCostAttributesBySalableProductStoreCollection, "SELECT NEW "+
				Cost.class.getName()+"(SUM(r.cost.numberOfProceedElements),SUM(r.cost.value),SUM(r.cost.tax),SUM(r.cost.turnover),SUM(r.cost.reduction),SUM(r.cost.commission))"
				+ " FROM SalableProductStoreCollectionItem r WHERE r.collection = :collection");
	}
	
	@Override
	public Collection<SalableProductStoreCollectionItem> readByCollectionBySalableProductStore(SalableProductStoreCollection salableProductStoreCollection, SalableProductStore salableProductStore) {
		return namedQuery(readByCollectionBySalableProductStore).parameter(SalableProductStoreCollectionItem.FIELD_COLLECTION, salableProductStoreCollection)
				.parameter(SalableProductStoreCollectionItem.FIELD_SALABLE_PRODUCT_STORE, salableProductStore).resultMany();
	}

	@Override
	public Cost sumCostBySalableProductStoreCollection(SalableProductStoreCollection salableProductStoreCollection) {
		return namedQuery(sumCostAttributesBySalableProductStoreCollection,Cost.class).parameter(SalableProductStoreCollectionItem.FIELD_COLLECTION, salableProductStoreCollection)
				.nullValue(new Cost()).resultOne();
	}
	
	
	
}
 