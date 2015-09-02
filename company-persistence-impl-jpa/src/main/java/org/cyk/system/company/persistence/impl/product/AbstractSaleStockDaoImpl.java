package org.cyk.system.company.persistence.impl.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.AbstractSaleStockDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public abstract class AbstractSaleStockDaoImpl<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends AbstractTypedDao<SALE_STOCK> implements AbstractSaleStockDao<SALE_STOCK,SEARCH_CRITERIA> {

	private static final long serialVersionUID = 6920278182318788380L;

	protected String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,
		readByTangibleProductStockMovements,computeByCriteria;
	
	@Inject protected SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,
    			_select().orderBy(fieldPath(SaleStock.FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT,TangibleProductStockMovement.FIELD_DATE), Boolean.TRUE));
    	registerNamedQuery(readByTangibleProductStockMovements,_select().whereIdentifierIn(SaleStock.FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT) );
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
	
	protected Object[] getComputeByCriteriaResults(SEARCH_CRITERIA criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(computeByCriteria, Object.class);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		return (Object[]) queryWrapper.resultOne();
	}

	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		AbstractSaleStockSearchCriteria saleStockSearchCriteria = (AbstractSaleStockSearchCriteria) searchCriteria;
		String externalIdentifier = saleStockSearchCriteria.getExternalIdentifierStringSearchCriteria().getPreparedValue();
		queryWrapper.parameterLike(SaleStockInput.FIELD_EXTERNAL_IDENTIFIER, externalIdentifier);
		queryWrapper.parameter(AbstractSaleStockSearchCriteria.FIELD_MINIMUM_QUANTITY, saleStockSearchCriteria.getMinimumQuantity());
		queryWrapper.parameter(Sale.FIELD_DONE, saleStockSearchCriteria.getDone());
	}
}
