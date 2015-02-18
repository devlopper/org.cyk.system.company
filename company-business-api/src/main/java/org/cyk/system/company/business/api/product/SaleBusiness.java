package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleBusiness extends TypedBusiness<Sale> {

	void process(Sale sale);
	
}
