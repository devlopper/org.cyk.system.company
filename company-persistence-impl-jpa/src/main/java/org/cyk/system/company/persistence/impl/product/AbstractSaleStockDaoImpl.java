package org.cyk.system.company.persistence.impl.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.AbstractSaleStockDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public abstract class AbstractSaleStockDaoImpl<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends AbstractTypedDao<SALE_STOCK> implements AbstractSaleStockDao<SALE_STOCK,SEARCH_CRITERIA> {

	private static final long serialVersionUID = 6920278182318788380L;

	protected String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,
		readByTangibleProductStockMovements;
	
	@Inject protected SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,_select().orderBy("tangibleProductStockMovement.date", Boolean.TRUE));
    	registerNamedQuery(readByTangibleProductStockMovements,_select().whereIdentifierIn("tangibleProductStockMovement") );
    }
	
	@Override
	public Collection<SALE_STOCK> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SALE_STOCK> readByCriteria(SEARCH_CRITERIA searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<SALE_STOCK>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(SEARCH_CRITERIA searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}	
	
	@Override
	public Collection<SALE_STOCK> readByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements) {
		if(tangibleProductStockMovements==null || tangibleProductStockMovements.isEmpty())
			return new ArrayList<SALE_STOCK>();
		return namedQuery(readByTangibleProductStockMovements).parameterIdentifiers(tangibleProductStockMovements).resultMany();
	}

	@Override
	public SALE_STOCK readByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement) {
		if(tangibleProductStockMovement==null)
			return null;
		Collection<SALE_STOCK> collection = readByTangibleProductStockMovements(Arrays.asList(tangibleProductStockMovement));
		return collection.isEmpty()?null:collection.iterator().next();
	}

	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		AbstractSaleStockSearchCriteria saleStockSearchCriteria = (AbstractSaleStockSearchCriteria) searchCriteria;
		String externalIdentifier = saleStockSearchCriteria.getExternalIdentifierStringSearchCriteria().getPreparedValue();
		queryWrapper.parameterLike("externalIdentifier", externalIdentifier);
		queryWrapper.parameter("minimumQuantity", saleStockSearchCriteria.getMinimumQuantity());
		queryWrapper.parameter("saleDone", saleStockSearchCriteria.getDone());
	}
}
