package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.AbstractSaleStockDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;

public abstract class AbstractSaleStockBusinessImpl<SALE_STOCK extends SaleStock,DAO extends AbstractSaleStockDao<SALE_STOCK, SEARCH_CRITERIA>,SEARCH_CRITERIA extends AbstractPeriodSearchCriteria> extends AbstractTypedBusinessService<SALE_STOCK, DAO> implements AbstractSaleStockBusiness<SALE_STOCK,SEARCH_CRITERIA>,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	public AbstractSaleStockBusinessImpl(DAO dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SALE_STOCK> findByCriteria(SEARCH_CRITERIA criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SEARCH_CRITERIA criteria) {
		return dao.countByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SALE_STOCK> findByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements) {
		return dao.readByTangibleProductStockMovements(tangibleProductStockMovements);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SALE_STOCK findByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement) {
		return dao.readByTangibleProductStockMovement(tangibleProductStockMovement);
	}
	
	
	

}
