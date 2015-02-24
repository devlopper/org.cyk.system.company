package org.cyk.system.company.persistence.impl.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.product.BalanceType;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleDaoImpl extends AbstractTypedDao<Sale> implements SaleDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final BigDecimal BALANCE_MIN=new BigDecimal("-1"+StringUtils.repeat('0', 18)),BALANCE_MAX=new BigDecimal("1"+StringUtils.repeat('0', 18));
	private static final BigDecimal BALANCE_ZERO_MIN=new BigDecimal("-0."+StringUtils.repeat('0', 18)+"1"),BALANCE_ZERO_MAX=new BigDecimal("0."+StringUtils.repeat('0', 18)+"1");
	
	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT sale FROM Sale sale ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE sale.date BETWEEN :fromDate AND :toDate AND sale.balance BETWEEN :minBalance AND :maxBalance ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	/*
	private static final String READ_BY_CRITERIA_WITHBALANCE_NOTORDERED_FORMAT = 
			READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT;
	*/
	//private static final String READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT = 
	//		READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT+ORDER_BY_FORMAT;
	
	private String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,sumBalanceByCriteria,
		sumCostByCriteria/*,readByCriteriaWithBalanceDateAscendingOrder,readByCriteriaWithBalanceDateDescendingOrder,sumBalanceByCriteriaWithBalance*/;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(sumCostByCriteria,"SELECT SUM(sale.cost) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	registerNamedQuery(sumBalanceByCriteria,"SELECT SUM(sale.balance) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	//registerNamedQuery(sumBalanceByCriteriaWithBalance,"SELECT SUM(sale.balance) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT);
    	registerNamedQuery(readAllSortedByDate,READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY sale.date DESC");
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT);
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date DESC") );
        
        //registerNamedQuery(readByCriteriaWithBalanceDateAscendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date ASC") );
        //registerNamedQuery(readByCriteriaWithBalanceDateDescendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date DESC") );
        
    }	
	
	/**/
	
	@Override
	public Collection<Sale> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sale> readByCriteria(SaleSearchCriteria searchCriteria) {
		String queryName = null;
		if(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered()!=null){
			queryName = Boolean.TRUE.equals(searchCriteria.getFromDateSearchCriteria().getAscendingOrdered())?
					readByCriteriaDateAscendingOrder:readByCriteriaDateDescendingOrder;
		}else
			queryName = readByCriteriaDateAscendingOrder;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<Sale>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(SaleSearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}
	
	@Override
	public BigDecimal sumCostByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(sumCostByCriteria, BigDecimal.class).nullValue(BigDecimal.ZERO);
		applyCriteriaParameters(queryWrapper, criteria);
		return (BigDecimal) queryWrapper.resultOne();
	}
	
	@Override
	public BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(sumBalanceByCriteria, BigDecimal.class).nullValue(BigDecimal.ZERO);
		applyCriteriaParameters(queryWrapper, criteria);
		return (BigDecimal) queryWrapper.resultOne();
	}
	
	/**/
	
	protected void applyCriteriaParameters(QueryWrapper<?> queryWrapper,SaleSearchCriteria searchCriteria){
		queryWrapper.parameter("fromDate",searchCriteria.getFromDateSearchCriteria().getPreparedValue());
		queryWrapper.parameter("toDate",searchCriteria.getToDateSearchCriteria().getPreparedValue());
		BigDecimal minBalance=BALANCE_ZERO_MIN,maxBalance=BALANCE_ZERO_MAX;
		if(searchCriteria.getBalanceTypes().contains(BalanceType.NEGAITVE)){
			minBalance = BALANCE_MIN;
			maxBalance = BALANCE_ZERO_MIN;
		}else if(searchCriteria.getBalanceTypes().contains(BalanceType.ZERO)){
			minBalance = BigDecimal.ZERO;
			maxBalance = BigDecimal.ZERO;
		}else if(searchCriteria.getBalanceTypes().contains(BalanceType.POSITIVE)){
			minBalance = BALANCE_ZERO_MAX;
			maxBalance = BALANCE_MAX;
		}else{
			minBalance = BALANCE_MIN;
			maxBalance = BALANCE_MAX;
		}
		queryWrapper.parameter("minBalance",minBalance);
		queryWrapper.parameter("maxBalance",maxBalance);
	}
	
	
	
}
