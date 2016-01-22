package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

public class StockableTangibleProductBusinessImpl extends AbstractTypedBusinessService<StockableTangibleProduct, StockableTangibleProductDao> implements StockableTangibleProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	private static final String INPUT_LABEL = "Input";
	private static final String OUTPUT_LABEL = "Output";
	
	@Inject
	public StockableTangibleProductBusinessImpl(StockableTangibleProductDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StockableTangibleProduct instanciate(TangibleProduct tangibleProduct) {
		StockableTangibleProduct stockableTangibleProduct = new StockableTangibleProduct();
		stockableTangibleProduct.setTangibleProduct(tangibleProduct);
		stockableTangibleProduct.setMovementCollection(RootBusinessLayer.getInstance().getMovementCollectionBusiness().instanciate(tangibleProduct.getCode(), INPUT_LABEL,OUTPUT_LABEL));
		return stockableTangibleProduct;
	}
	
	@Override
	public StockableTangibleProduct create(StockableTangibleProduct stockableTangibleProduct) {
		if(stockableTangibleProduct.getMovementCollection()==null)
			stockableTangibleProduct.setMovementCollection(RootBusinessLayer.getInstance().getMovementCollectionBusiness()
					.instanciate(stockableTangibleProduct.getTangibleProduct().getCode(), INPUT_LABEL,OUTPUT_LABEL));
		RootBusinessLayer.getInstance().getMovementCollectionBusiness().create(stockableTangibleProduct.getMovementCollection());
		return super.create(stockableTangibleProduct);
	}

}
