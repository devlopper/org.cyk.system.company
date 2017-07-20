package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleProductInstanceDao extends TypedDao<SaleProductInstance> {

	Collection<SaleProductInstance> readBySaleProduct(SalableProductCollectionItem salableProductCollectionItem);

	SaleProductInstance readBySalableProductInstanceCode(String code);
	
}
