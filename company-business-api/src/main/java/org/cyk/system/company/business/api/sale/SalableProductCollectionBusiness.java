package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;

public interface SalableProductCollectionBusiness extends AbstractCollectionBusiness<SalableProductCollection,SalableProductCollectionItem> {
    
	SalableProductCollectionItem add(SalableProductCollection salableProductCollection,SalableProductCollectionItem salableProductCollectionItem);
	SalableProductCollectionItem remove(SalableProductCollection salableProductCollection,SalableProductCollectionItem salableProductCollectionItem);
	/*void remove(SalableProductCollection sale,SalableProductCollectionItem saleProduct);
	void applyChange(SalableProductCollection sale, SalableProductCollectionItem salableProductCollectionItem);*/
	
	void computeCost(SalableProductCollection salableProductCollection,Collection<SalableProductCollectionItem> salableProductCollectionItems);
	void computeCost(SalableProductCollection salableProductCollection);
}
