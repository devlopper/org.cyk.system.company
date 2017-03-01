package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.persistence.api.AbstractCollectionItemDao;

public interface SalableProductCollectionItemDao extends AbstractCollectionItemDao<SalableProductCollectionItem,SalableProductCollection> {

	Collection<SalableProductCollectionItem> readByCollectionBySalableProduct(SalableProductCollection salableProductCollection,SalableProduct salableProduct);
	
}
