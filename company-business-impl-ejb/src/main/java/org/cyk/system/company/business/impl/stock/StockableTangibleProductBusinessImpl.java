package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

public class StockableTangibleProductBusinessImpl extends AbstractTypedBusinessService<StockableTangibleProduct, StockableTangibleProductDao> implements StockableTangibleProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableTangibleProductBusinessImpl(StockableTangibleProductDao dao) {
		super(dao);
	}
	
	@Override
	public StockableTangibleProduct create(StockableTangibleProduct stockableTangibleProduct) {
		RootBusinessLayer.getInstance().getMovementCollectionBusiness().create(stockableTangibleProduct.getMovementCollection());
		return super.create(stockableTangibleProduct);
	}

}
