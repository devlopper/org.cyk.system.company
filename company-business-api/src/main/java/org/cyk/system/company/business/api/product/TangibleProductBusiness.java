package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.TangibleProduct;

public interface TangibleProductBusiness extends AbstractProductBusiness<TangibleProduct> {

	void setIsStockable(TangibleProduct tangibleProduct);
	
}
