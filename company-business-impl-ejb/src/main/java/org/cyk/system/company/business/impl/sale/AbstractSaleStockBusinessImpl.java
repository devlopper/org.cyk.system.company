package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.sale.AbstractSaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.model.sale.AbstractSaleStockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.AbstractSaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public abstract class AbstractSaleStockBusinessImpl<SALE extends SaleStockTangibleProductMovement,DAO extends AbstractSaleStockTangibleProductMovementDao<SALE, SEARCH_CRITERIA>,SEARCH_CRITERIA extends AbstractSaleStockTangibleProductMovementSearchCriteria> extends AbstractTypedBusinessService<SALE, DAO> implements AbstractSaleStockTangibleProductMovementBusiness<SALE,SEARCH_CRITERIA>,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	public AbstractSaleStockBusinessImpl(DAO dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SALE> findByCriteria(SEARCH_CRITERIA criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SEARCH_CRITERIA criteria) {
		return dao.countByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SALE> findByStockTangibleProductStockMovements(Collection<StockTangibleProductMovement> stockTangibleProductStockMovements) {
		return dao.readByStockTangibleProductStockMovements(stockTangibleProductStockMovements);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SALE findByStockTangibleProductStockMovement(StockTangibleProductMovement stockTangibleProductStockMovement) {
		return dao.readByStockTangibleProductStockMovement(stockTangibleProductStockMovement);
	}
	
}
