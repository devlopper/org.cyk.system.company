package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;
import org.cyk.utility.common.LogMessage;

public interface SalableProductCollectionBusiness extends AbstractCollectionBusiness<SalableProductCollection,SalableProductCollectionItem> {
    
	SalableProductCollection instanciateOne(String code,String name,Cost cost,Object[][] salableProducts);
	SalableProductCollection instanciateOne(String code,Object[][] salableProducts);
	
	void computeCost(SalableProductCollection salableProductCollection,Collection<SalableProductCollectionItem> salableProductCollectionItems,LogMessage.Builder logMessageBuilder);
	void computeCost(SalableProductCollection salableProductCollection,Collection<SalableProductCollectionItem> salableProductCollectionItems);
	void computeCost(SalableProductCollection salableProductCollection,LogMessage.Builder logMessageBuilder);
	void computeCost(SalableProductCollection salableProductCollection);
	void computeDerivationsFromCost(SalableProductCollection salableProductCollection);
}
