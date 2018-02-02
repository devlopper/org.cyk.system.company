package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;

public interface SalableProductCollectionBusiness extends AbstractCollectionBusiness<SalableProductCollection,SalableProductCollectionItem> {
    
	SalableProductCollection instanciateOne(String code,String name,Cost cost,Object[][] salableProducts);
	SalableProductCollection instanciateOne(String code,Object[][] salableProducts);
	
}
