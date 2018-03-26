package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.root.persistence.api.AbstractCollectionItemDao;

public interface SalableProductStoreCollectionItemDao extends AbstractCollectionItemDao<SalableProductStoreCollectionItem,SalableProductStoreCollection> {

	Collection<SalableProductStoreCollectionItem> readByCollectionBySalableProductStore(SalableProductStoreCollection salableProductStoreCollection,SalableProductStore salableProductStore);
	
	/**
	 * Sum cost number of proceed elements , value , tax and turnover
	 * @param salableProductStoreCollection
	 * @return
	 */
	Cost sumCostBySalableProductStoreCollection(SalableProductStoreCollection salableProductStoreCollection);
}
