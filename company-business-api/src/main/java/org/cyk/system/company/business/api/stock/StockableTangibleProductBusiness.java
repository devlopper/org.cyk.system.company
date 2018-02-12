package org.cyk.system.company.business.api.stock;

import java.util.List;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.business.api.TypedBusiness;

@Deprecated
public interface StockableTangibleProductBusiness extends TypedBusiness<StockableTangibleProduct> {

	StockableTangibleProduct instanciateOne(TangibleProduct tangibleProduct);
	StockableTangibleProduct instanciateOne(String tangibleProductCode);
	List<StockableTangibleProduct> instanciateMany(String[][] arguments);
}
