package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleProductBusiness extends TypedBusiness<SaleProduct> {

	void process(SaleProduct saleProduct);
	
}
