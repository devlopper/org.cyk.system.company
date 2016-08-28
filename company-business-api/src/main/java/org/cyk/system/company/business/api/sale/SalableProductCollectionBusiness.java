package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;

public interface SalableProductCollectionBusiness extends AbstractCollectionBusiness<SalableProductCollection,SalableProductCollectionItem> {
    
	SalableProductCollectionItem add(SalableProductCollection salableProductCollection,SalableProductCollectionItem salableProductCollectionItem);
	/*SalableProductCollectionItem remove(SalableProductCollection salableProductCollection,SalableProduct salableProduct);
	void remove(SalableProductCollection sale,SalableProductCollectionItem saleProduct);
	void applyChange(SalableProductCollection sale, SalableProductCollectionItem salableProductCollectionItem);*/
	
    
}
