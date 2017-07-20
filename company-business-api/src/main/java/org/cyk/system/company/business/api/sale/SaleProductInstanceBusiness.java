package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleProductInstanceBusiness extends TypedBusiness<SaleProductInstance> {

	Collection<SaleProductInstance> findBySaleProduct(SalableProductCollectionItem saleProduct);
	SaleProductInstance findBySalableProductInstanceCode(String code);
	
}
