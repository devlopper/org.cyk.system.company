package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.product.TangibleProductStockMovementSearchCriteria;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.product.TangibleProductStockMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class TangibleProductStockMovementBusinessImpl extends AbstractTypedBusinessService<TangibleProductStockMovement, TangibleProductStockMovementDao> implements TangibleProductStockMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductDao productDao;
	
	@Inject
	public TangibleProductStockMovementBusinessImpl(TangibleProductStockMovementDao dao) {
		super(dao);
	}
	
	@Override
	public TangibleProductStockMovement create(TangibleProductStockMovement tangibleProductStockMovement) {
		logDebug("Create tangible product stock movement");
		if(tangibleProductStockMovement.getDate()==null)
			tangibleProductStockMovement.setDate(universalTimeCoordinated());
		updateStock(tangibleProductStockMovement);
		//notifyCrudDone(Crud.CREATE, tangibleProductStockMovement);
		logDebug("Tangible product stock movement created successfully. Product={} Q={}",tangibleProductStockMovement.getTangibleProduct().getCode(),
				tangibleProductStockMovement.getQuantity());
		return super.create(tangibleProductStockMovement);
	}
	
	@Override
	public TangibleProductStockMovement update(TangibleProductStockMovement tangibleProductStockMovement) {
		updateStock(tangibleProductStockMovement);
		return super.update(tangibleProductStockMovement);
	}
	
	private void updateStock(TangibleProductStockMovement tangibleProductStockMovement){
		exceptionUtils().exception(tangibleProductStockMovement.getQuantity().signum()==0, "exception.tangibleproductstockmovement.quantity.mustbegreaterthanzero");
		BigDecimal stock = tangibleProductStockMovement.getTangibleProduct().getStockQuantity();
		if(stock==null)
			stock = BigDecimal.ZERO;
		BigDecimal newStock = stock.add(tangibleProductStockMovement.getQuantity());
		exceptionUtils().exception(newStock.signum()==-1, "tangibleproduct.stock.negative");
		tangibleProductStockMovement.getTangibleProduct().setStockQuantity(newStock);
		productDao.update(tangibleProductStockMovement.getTangibleProduct());
	}

	@Override
	public Collection<TangibleProductStockMovement> findByCriteria(TangibleProductStockMovementSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override
	public Long countByCriteria(TangibleProductStockMovementSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
}
