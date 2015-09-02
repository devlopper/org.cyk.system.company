package org.cyk.system.company.persistence.impl.product;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SalesDetails;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.computation.LogicalOperator;

public class SaleDaoImpl extends AbstractTypedDao<Sale> implements SaleDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final BigDecimal BALANCE_MIN=new BigDecimal("-1"+StringUtils.repeat('0', 18)),BALANCE_MAX=new BigDecimal("1"+StringUtils.repeat('0', 18));
	private static final BigDecimal BALANCE_ZERO_MIN=new BigDecimal("-0."+StringUtils.repeat('0', 18)+"1"),BALANCE_ZERO_MAX=new BigDecimal("0."+StringUtils.repeat('0', 18)+"1");
	
	private String readAllSortedByDate,readByCriteria,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,computeByCriteria
		,readByComputedIdentifier/*,sumBalanceByCustomerCriteria*/;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	
    	QueryStringBuilder queryStringBuilder = _select();
    	whereSearchCriteria(queryStringBuilder);
    	
    	/*.where(Sale.FIELD_COMPUTED_IDENTIFIER,ArithmeticOperator.LIKE)
    			.and().between(Sale.FIELD_DATE).and().between(Balance.FIELD_VALUE, PARAM_BALANCE_MIN, PARAM_BALANCE_MAX)
    			.where(LogicalOperator.AND,Sale.FIELD_DONE,PARAM_SALE_DONE,ArithmeticOperator.EQ);*/
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy(Sale.FIELD_DATE, Boolean.TRUE).getValue();
    	registerNamedQuery(readAllSortedByDate,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy(Sale.FIELD_DATE, Boolean.FALSE));
    	
        queryStringBuilder = _selectString(sumAttributes(Sale.FIELD_COST,Sale.FIELD_TURNOVER,Sale.FIELD_VALUE_ADDED_TAX
        		,commonUtils.attributePath(Sale.FIELD_BALANCE, Balance.FIELD_VALUE)));
        
    	whereSearchCriteria(queryStringBuilder);
        //System.out.println(queryStringBuilder.getValue());
    	registerNamedQuery(computeByCriteria,queryStringBuilder);
    	//registerNamedQuery(sumValueAddedTaxByCriteria,"SELECT SUM(sale.valueAddedTax) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	//registerNamedQuery(sumBalanceByCriteria,"SELECT SUM(sale.balance.value) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT);
    	//registerNamedQuery(sumBalanceByCustomerCriteria,"SELECT SUM(sale.balance.value) FROM Sale sale WHERE sale.customer.identifier IN :identifiers AND sale.done = :done");
    	//registerNamedQuery(sumBalanceByCriteriaWithBalance,"SELECT SUM(sale.balance) FROM Sale sale "+READ_BY_CRITERIA_WHERE_FORMAT+READ_BY_CRITERIA_WHERE_BALANCE_FORMAT);
    	
    	//registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY sale.date ASC");
        //registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date ASC") );
        //registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sale.date DESC") );
        registerNamedQuery(readByComputedIdentifier, _select().where(Sale.FIELD_COMPUTED_IDENTIFIER));
        //registerNamedQuery(readByPeriod,"SELECT sale FROM Sale sale WHERE sale.date BETWEEN :fromDate AND :toDate");
        
        //registerNamedQuery(readByCriteriaWithBalanceDateAscendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date ASC") );
        //registerNamedQuery(readByCriteriaWithBalanceDateDescendingOrder,String.format(READ_BY_CRITERIA_WITHBALANCE_ORDERED_FORMAT, "sale.date DESC") );
        
        //"WHERE sale.date BETWEEN :fromvalue AND :tovalue AND sale.balance.value BETWEEN :minBalance AND :maxBalance AND sale.done = :done ";
    }	
	
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		queryStringBuilder.where(Sale.FIELD_COMPUTED_IDENTIFIER,ArithmeticOperator.LIKE)
		.and().between(Sale.FIELD_DATE)
		.and()
		.between(commonUtils.attributePath(Sale.FIELD_BALANCE,Balance.FIELD_VALUE), Constant.CHARACTER_COLON+PARAM_BALANCE_MIN, Constant.CHARACTER_COLON+PARAM_BALANCE_MAX)
		.where(LogicalOperator.AND,Sale.FIELD_DONE,Sale.FIELD_DONE,ArithmeticOperator.EQ);
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
	public SalesDetails computeByCriteria(SaleSearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(computeByCriteria,Object.class);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		
		Object[] values = (Object[]) queryWrapper.resultOne();
		SalesDetails results = new SalesDetails();
		results.setCost((BigDecimal) values[0]);
		results.setTurnover((BigDecimal) values[1]);
		results.setValueAddedTax((BigDecimal) values[2]);
		results.setBalance((BigDecimal) values[3]);
		return results;
	}
	
	/**/
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleSearchCriteria saleSearchCriteria = (SaleSearchCriteria) searchCriteria;
		queryWrapper.parameterLike(Sale.FIELD_COMPUTED_IDENTIFIER,saleSearchCriteria.getComputedIdentifierStringSearchCriteria().getPreparedValue());
		queryWrapper.parameter(Sale.FIELD_DONE,saleSearchCriteria.getDone());
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
		queryWrapper.parameter(PARAM_BALANCE_MIN,minBalance);
		queryWrapper.parameter(PARAM_BALANCE_MAX,maxBalance);
	}
	
	@Override
	public Sale readByComputedIdentifier(String computedIdentifier) {
		return namedQuery(readByComputedIdentifier).parameter(Sale.FIELD_COMPUTED_IDENTIFIER, computedIdentifier)
				.ignoreThrowable(NoResultException.class).resultOne();
	}
	
	public static final String PARAM_BALANCE_MIN = "minBalance";
	public static final String PARAM_BALANCE_MAX = "maxBalance";

}
