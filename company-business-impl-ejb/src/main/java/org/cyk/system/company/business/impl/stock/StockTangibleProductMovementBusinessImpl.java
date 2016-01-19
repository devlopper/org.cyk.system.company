package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.persistence.api.stock.StockTangibleProductMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

@Stateless
public class StockTangibleProductMovementBusinessImpl extends AbstractTypedBusinessService<StockTangibleProductMovement, StockTangibleProductMovementDao> implements StockTangibleProductMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public StockTangibleProductMovementBusinessImpl(StockTangibleProductMovementDao dao) {
		super(dao);
	}
	
	@Override
	public StockTangibleProductMovement create(StockTangibleProductMovement tangibleProductStockMovement) {
		RootBusinessLayer.getInstance().getMovementBusiness().create(tangibleProductStockMovement.getMovement());
		//updateStock(tangibleProductStockMovement);
		tangibleProductStockMovement = super.create(tangibleProductStockMovement);
		logIdentifiable("Created", tangibleProductStockMovement);
		return tangibleProductStockMovement;
	}
	/*
	private void updateStock(TangibleProductStockMovement tangibleProductStockMovement){
		StockConfiguration stockConfiguration = accountingPeriodBusiness.findCurrent().getStockConfiguration();
		BigDecimal stock = tangibleProductStockMovement.get;
		if(stock==null)
			stock = BigDecimal.ZERO;
		BigDecimal newStock = stock.add(tangibleProductStockMovement.getMovement().getValue());
		exceptionUtils().exception(newStock.signum()==-1, "tangibleproduct.stock.negative");
		tangibleProductStockMovement.getTangibleProduct().setStockQuantity(newStock);
		productDao.update(tangibleProductStockMovement.getTangibleProduct());
	}*/

	@Override
	public Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override
	public Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
}
