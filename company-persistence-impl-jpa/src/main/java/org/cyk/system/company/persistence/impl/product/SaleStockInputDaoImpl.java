package org.cyk.system.company.persistence.impl.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleStockInputDaoImpl extends AbstractTypedDao<SaleStockInput> implements SaleStockInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT ssi FROM SaleStockInput ssi ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE ssi.sale.date BETWEEN :fromDate AND :toDate AND ssi.sale.done = :saleDone AND ssi.remainingNumberOfGoods >= :minimumRemainingGoods ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	private String readAllSortedByDate,readByIdentificationNumber,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,
		readBySales;
	
	@Inject private SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY ssi.sale.date ASC");
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY ssi.sale.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ssi.sale.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ssi.sale.date DESC") );
        registerNamedQuery(readByIdentificationNumber,_select().where("sale.identificationNumber","identificationNumber") );
        registerNamedQuery(readBySales,_select().whereIdentifierIn("sale") );
        
    }

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SaleStockInput> readByCriteria(SaleStockInputSearchCriteria searchCriteria) {
		if(StringUtils.isNotBlank(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue())){
			Sale sale = saleDao.readByIdentificationNumber(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue());
			if(sale==null)
				return new ArrayList<SaleStockInput>();
			SaleStockInput saleStockInput = readBySale(sale);
			if(saleStockInput==null)
				return new ArrayList<SaleStockInput>();
			return Arrays.asList(saleStockInput);
		}
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<SaleStockInput>) queryWrapper.resultMany();
	}
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleStockInputSearchCriteria saleStockInputSearchCriteria = (SaleStockInputSearchCriteria) searchCriteria;
		queryWrapper.parameter("saleDone", saleStockInputSearchCriteria.getDone());
		queryWrapper.parameter("minimumRemainingGoods", saleStockInputSearchCriteria.getMinimumRemainingGoodsCount());
	}

	@Override
	public Long countByCriteria(SaleStockInputSearchCriteria searchCriteria) {
		if(StringUtils.isNotBlank(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue())){
			return readBySale(saleDao.readByIdentificationNumber(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue()))==null?0l:1l;
		}
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}	

	@Override
	public Collection<SaleStockInput> readBySales(Collection<Sale> sales) {
		if(sales==null || sales.isEmpty())
			return new ArrayList<SaleStockInput>();
		return namedQuery(readBySales).parameterIdentifiers(sales).resultMany();
	}

	@Override
	public SaleStockInput readBySale(Sale sale) {
		if(sale==null)
			return null;
		Collection<SaleStockInput> collection = readBySales(Arrays.asList(sale));
		return collection.isEmpty()?null:collection.iterator().next();
	}
	
}
