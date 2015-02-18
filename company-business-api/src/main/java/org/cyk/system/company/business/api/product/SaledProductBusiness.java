package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaledProductBusiness extends TypedBusiness<SaledProduct> {

	void process(SaledProduct saledProduct);
	
}
