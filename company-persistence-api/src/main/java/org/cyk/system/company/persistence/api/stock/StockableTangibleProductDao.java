package org.cyk.system.company.persistence.api.stock;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.persistence.api.TypedDao;

@Deprecated
public interface StockableTangibleProductDao extends TypedDao<StockableTangibleProduct> {
	
	StockableTangibleProduct readByTangibleProduct(TangibleProduct tangibleProduct);
	
}
