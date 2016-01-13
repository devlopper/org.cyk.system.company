package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SalableProductBusiness extends TypedBusiness<SalableProduct> {

	void consume(Sale sale);
	
}
