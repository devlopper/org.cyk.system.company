package org.cyk.system.company.persistence.impl.stock;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.persistence.api.stock.StockableProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.utility.common.helper.CollectionHelper;

public class StockableProductDaoImpl extends AbstractTypedDao<StockableProduct> implements StockableProductDao {
	private static final long serialVersionUID = 6920278182318788380L;

	@Override
	public StockableProduct readByProduct(Product product) {
		return CollectionHelper.getInstance().getFirst(readByFilter(new StockableProduct.Filter().addMaster(product)));
	}

}
