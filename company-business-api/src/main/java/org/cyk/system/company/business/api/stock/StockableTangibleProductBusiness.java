package org.cyk.system.company.business.api.stock;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface StockableTangibleProductBusiness extends TypedBusiness<StockableTangibleProduct> {

	StockableTangibleProduct instanciate(TangibleProduct tangibleProduct);
	
}
