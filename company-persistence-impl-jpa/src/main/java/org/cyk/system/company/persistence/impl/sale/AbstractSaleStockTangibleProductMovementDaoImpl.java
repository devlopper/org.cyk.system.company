package org.cyk.system.company.persistence.impl.sale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.model.sale.AbstractSaleStockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.persistence.api.sale.AbstractSaleStockTangibleProductMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public abstract class AbstractSaleStockTangibleProductMovementDaoImpl<SALE extends SaleStockTangibleProductMovement,SEARCH_CRITERIA extends AbstractSaleStockTangibleProductMovementSearchCriteria> extends AbstractTypedDao<SALE> implements AbstractSaleStockTangibleProductMovementDao<SALE,SEARCH_CRITERIA> {

	private static final long serialVersionUID = 6920278182318788380L;

	protected String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,
		readByTangibleProductStockMovements,computeByCriteria;
	
	@Inject protected SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	/*registerNamedQuery(readAllSortedByDate,
    			_select().orderBy(fieldPath(SaleStockTangibleProductMovement.FIELD_STOCK_TANGIBLE_PRODUCT_STOCK_MOVEMENT
    					,StockTangibleProductMovement.FIELD_MOVEMENT,Movement.FIELD_DATE), Boolean.TRUE));
    	registerNamedQuery(readByTangibleProductStockMovements,_select().whereIdentifierIn(fieldPath(SaleStockTangibleProductMovement.FIELD_STOCK_TANGIBLE_PRODUCT_STOCK_MOVEMENT
				,StockTangibleProductMovement.FIELD_MOVEMENT)));*/
    }
	
	@Override
	public Collection<SALE> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SALE> readByCriteria(SEARCH_CRITERIA searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<SALE>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(SEARCH_CRITERIA searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}	
	
	@Override
	public Collection<SALE> readByStockTangibleProductStockMovements(Collection<StockTangibleProductMovement> tangibleProductStockMovements) {
		if(tangibleProductStockMovements==null || tangibleProductStockMovements.isEmpty())
			return new ArrayList<SALE>();
		return namedQuery(readByTangibleProductStockMovements).parameterIdentifiers(tangibleProductStockMovements).resultMany();
	}

	@Override
	public SALE readByStockTangibleProductStockMovement(StockTangibleProductMovement tangibleProductStockMovement) {
		if(tangibleProductStockMovement==null)
			return null;
		Collection<SALE> collection = readByStockTangibleProductStockMovements(Arrays.asList(tangibleProductStockMovement));
		return collection.isEmpty()?null:collection.iterator().next();
	}
	
	protected Object[] getComputeByCriteriaResults(SEARCH_CRITERIA criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(computeByCriteria, Object.class);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		return (Object[]) queryWrapper.resultOne();
	}

	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		AbstractSaleStockTangibleProductMovementSearchCriteria saleStockSearchCriteria = (AbstractSaleStockTangibleProductMovementSearchCriteria) searchCriteria;
		String externalIdentifier = saleStockSearchCriteria.getExternalIdentifierStringSearchCriteria().getPreparedValue();
		queryWrapper.parameterLike(SaleStockTangibleProductMovementInput.FIELD_EXTERNAL_IDENTIFIER, externalIdentifier);
		queryWrapper.parameter(StockTangibleProductMovementSearchCriteria.FIELD_MINIMUM_QUANTITY, saleStockSearchCriteria.getStockTangibleProductMovementSearchCriteria().getMinimumQuantitySearchCriteria().getPreparedValue());
	}
}
