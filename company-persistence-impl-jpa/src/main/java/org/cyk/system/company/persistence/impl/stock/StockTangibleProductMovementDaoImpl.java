package org.cyk.system.company.persistence.impl.stock;

import java.util.Collection;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.persistence.api.stock.StockTangibleProductMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class StockTangibleProductMovementDaoImpl extends AbstractTypedDao<StockTangibleProductMovement> implements StockTangibleProductMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	/*private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT tpsm FROM TangibleProductStockMovement tpsm ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE tpsm.date BETWEEN :fromDate AND :toDate ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	*/
	private String /*readAllSortedByDate,readByCriteria,*/countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	//registerNamedQuery(readAllSortedByDate,_select().orderBy("date", Boolean.TRUE) /*READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY tpsm.date ASC"*/);
    	/*registerNamedQuery(readByCriteria, READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY tpsm.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "tpsm.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "tpsm.date DESC") );*/
    }

	@SuppressWarnings("unchecked")
	@Override
	public Collection<StockTangibleProductMovement> readByCriteria(StockTangibleProductMovementSearchCriteria searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<StockTangibleProductMovement>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(StockTangibleProductMovementSearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}	
	

	
}
