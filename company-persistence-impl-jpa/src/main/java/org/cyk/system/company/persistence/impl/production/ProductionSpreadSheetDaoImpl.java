package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class ProductionSpreadSheetDaoImpl extends AbstractTypedDao<ProductionSpreadSheet> implements ProductionSpreadSheetDao {

	private static final long serialVersionUID = 6920278182318788380L;
	
	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT ps FROM ProductionSpreadSheet ps ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE ps.period.fromDate BETWEEN :fromDate AND :toDate ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	private String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder;

	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY ps.period.fromDate ASC");
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY ps.period.fromDate ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ps.period.fromDate ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ps.period.fromDate DESC") );
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<ProductionSpreadSheet> readByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<ProductionSpreadSheet>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}
	
	protected void applyCriteriaParameters(QueryWrapper<?> queryWrapper,ProductionSpreadSheetSearchCriteria searchCriteria){
		queryWrapper.parameter("fromDate",searchCriteria.getFromDateSearchCriteria().getPreparedValue());
		queryWrapper.parameter("toDate",searchCriteria.getToDateSearchCriteria().getPreparedValue());
	}

	
	
}
