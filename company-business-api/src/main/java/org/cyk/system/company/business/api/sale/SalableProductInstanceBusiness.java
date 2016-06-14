package org.cyk.system.company.business.api.sale;

import java.util.Collection;
import java.util.Set;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.AbstractCollectionItemBusiness;

public interface SalableProductInstanceBusiness extends AbstractCollectionItemBusiness<SalableProductInstance,SalableProduct> {

	void create(SalableProduct salableProduct,Set<String> codes);
	
	Collection<SalableProductInstance> findWhereNotAssociatedToCashRegister();
}
