package org.cyk.system.company.persistence.impl.product;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleDaoImpl extends AbstractTypedDao<Sale> implements SaleDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final BigDecimal BALANCE_MIN=new BigDecimal("-1"+StringUtils.repeat('0', 18)),BALANCE_MAX=new BigDecimal("1"+StringUtils.repeat('0', 18));
	private static final BigDecimal BALANCE_ZERO_MIN=new BigDecimal("-0."+StringUtils.repeat('0', 18)+"1"),BALANCE_ZERO_MAX=new BigDecimal("0."+StringUtils.repeat('0', 18)+"1");
	
	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT sale FROM Sale sale ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE sale.date BETWEEN :fromvalue AND :tovalue AND sale.balance.value BETWEEN :minBalance AND :maxBalance AND sale.done = :done ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	/*
	private static final String READ_BY_CRITERIA_WITHBALANCE_NOTORDERED_FORMAT = 
			READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT;
	*/
	//private static final String READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT = 
	//		READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT+ORDER_BY_FORMAT;
	
	private String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,sumBalanceByCriteria,
		sumCostByCriteria,sumValueAddedTaxByCriteria,sumBalanceByCustomerCriteria,readByIdentificationNumber;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(sumCostByCriteria,"SELECT SUM(sale.cost) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	registerNamedQuery(sumValueAddedTaxByCriteria,"SELECT SUM(sale.valueAddedTax) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	registerNamedQuery(sumBalanceByCriteria,"SELECT SUM(sale.balance.value) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	registerNamedQuery(sumBalanceByCustomerCriteria,"SELECT SUM(sale.balance.value) FROM Sale sale WHERE sale.customer.identifier IN :identifiers AND sale.done = :done");
    	//registerNamedQuery(sumBalanceByCriteriaWithBalance,"SELECT SUM(sale.balance) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT);
    	registerNamedQuery(readAllSortedByDate,READ_BY_CRITERIA_SELECT_FORMAT+" ORDER BY sale.date ASC");
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY sale.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date DESC") );
        registerNamedQuery(readByIdentificationNumber, _select().where("identificationNumber"));
        //registerNamedQuery(readByPeriod,"SELECT sale FROM Sale sale WHERE sale.date BETWEEN :fromDate AND :toDate");
        
        //registerNamedQuery(readByCriteriaWithBalanceDateAscendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date ASC") );
        //registerNamedQuery(readByCriteriaWithBalanceDateDescendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date DESC") );
        
    }	
	
	/**/
	
	@Override
	public Collection<Sale> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
	/*
	@Override
	public Collection<Sale> readByPeriod(Period period) {
		return namedQuery(readByPeriod).resultMany();
	}
	*/
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
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<Sale>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(SaleSearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}
	
	@Override
	public BigDecimal sumCostByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(sumCostByCriteria, BigDecimal.class).nullValue(BigDecimal.ZERO);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		return (BigDecimal) queryWrapper.resultOne();
	}
	
	@Override
	public BigDecimal sumValueAddedTaxByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(sumValueAddedTaxByCriteria, BigDecimal.class).nullValue(BigDecimal.ZERO);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		return (BigDecimal) queryWrapper.resultOne();
	}
	
	@Override
	public BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(sumBalanceByCriteria, BigDecimal.class).nullValue(BigDecimal.ZERO);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		return (BigDecimal) queryWrapper.resultOne();
	}
	
	/**/
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleSearchCriteria saleSearchCriteria = (SaleSearchCriteria) searchCriteria;
		queryWrapper.parameter("done",saleSearchCriteria.getDone());
		//queryWrapper.parameterIdentifiers(saleSearchCriteria.getCustomers());
		BigDecimal minBalance=BALANCE_ZERO_MIN,maxBalance=BALANCE_ZERO_MAX;
		if(saleSearchCriteria.getBalanceTypes().contains(BalanceType.NEGAITVE)){
			minBalance = BALANCE_MIN;
			maxBalance = BALANCE_ZERO_MIN;
		}else if(saleSearchCriteria.getBalanceTypes().contains(BalanceType.ZERO)){
			minBalance = BigDecimal.ZERO;
			maxBalance = BigDecimal.ZERO;
		}else if(saleSearchCriteria.getBalanceTypes().contains(BalanceType.POSITIVE)){
			minBalance = BALANCE_ZERO_MAX;
			maxBalance = BALANCE_MAX;
		}else{
			minBalance = BALANCE_MIN;
			maxBalance = BALANCE_MAX;
		}
		queryWrapper.parameter("minBalance",minBalance);
		queryWrapper.parameter("maxBalance",maxBalance);
	}
	
	/*
	@Override
	public BigDecimal sumBalanceByCustomer(SaleSearchCriteria criteria) {
		return namedQuery(sumBalanceByCustomer, BigDecimal.class).parameterIdentifiers(criteria.getCustomers()).parameter("done", criteria.getDone())
				.nullValue(BigDecimal.ZERO).resultOne();
	}*/
	/*
	@Override
	public Collection<Sale> readByCustomer(Customer customer,
			Collection<BalanceType> balanceTypes) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Long countByCustomer(Customer customer,
			Collection<BalanceType> balanceTypes) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	@Override
	public Sale readByIdentificationNumber(String identificationNumber) {
		return namedQuery(readByIdentificationNumber).parameter("identificationNumber", identificationNumber)
				.ignoreThrowable(NoResultException.class).resultOne();
	}
	
	
	
}
