package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleStockOutputDaoImpl extends AbstractTypedDao<SaleStockOutput> implements SaleStockOutputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT sso FROM SaleStockOutput sso ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE sso.tangibleProductStockMovement.date BETWEEN :fromDate AND :toDate ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	private String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,readBySaleStockInput;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY sso.tangibleProductStockMovement.date ASC");
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY sso.tangibleProductStockMovement.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sso.tangibleProductStockMovement.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sso.tangibleProductStockMovement.date DESC") );
        registerNamedQuery(readBySaleStockInput,_select().where("saleStockInput"));
        
    }

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SaleStockOutput> readByCriteria(DefaultSearchCriteria searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<SaleStockOutput>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(DefaultSearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}	
	
	protected void applyCriteriaParameters(QueryWrapper<?> queryWrapper,DefaultSearchCriteria searchCriteria){
		queryWrapper.parameter("fromDate",searchCriteria.getFromDateSearchCriteria().getPreparedValue());
		queryWrapper.parameter("toDate",searchCriteria.getToDateSearchCriteria().getPreparedValue());
	}

	@Override
	public Collection<SaleStockOutput> readBySaleStockInput(SaleStockInput saleStockInput) {
		return namedQuery(readBySaleStockInput).parameter("saleStockInput", saleStockInput).resultMany();
	}
	
}
