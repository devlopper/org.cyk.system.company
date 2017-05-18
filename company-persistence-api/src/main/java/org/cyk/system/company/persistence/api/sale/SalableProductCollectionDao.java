package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.persistence.api.AbstractCollectionDao;

public interface SalableProductCollectionDao extends AbstractCollectionDao<SalableProductCollection,SalableProductCollectionItem> {
	
}
