package org.cyk.system.company.persistence.impl.stock;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.utility.common.helper.CollectionHelper;

public class StockableTangibleProductDaoImpl extends AbstractTypedDao<StockableTangibleProduct> implements StockableTangibleProductDao {
	private static final long serialVersionUID = 6920278182318788380L;

	@Override
	public StockableTangibleProduct readByTangibleProduct(TangibleProduct tangibleProduct) {
		return CollectionHelper.getInstance().getFirst(readByFilter(new StockableTangibleProduct.Filter().addMaster(tangibleProduct)));
	}

	

}
