package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleDaoImpl extends AbstractSaleDaoImpl<Sale,Sale.SearchCriteria> implements SaleDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readAllSortedByDate/*,readByComputedIdentifier,readByCriteria*/,countByCriteria,readByCriteriaDateAscendingOrder,readByCriteriaDateDescendingOrder,computeByCriteria
		,computeByCriteriaWhereCashRegisterMovementNotExists,computeByCriteriaWhereCashRegisterMovementExists;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	/*
    	String attributeCost = commonUtils.attributePath(Sale.FIELD_COST, Cost.FIELD_VALUE);
    	String attributeTax = commonUtils.attributePath(Sale.FIELD_COST, Cost.FIELD_TAX);
    	String attributeTurnover = commonUtils.attributePath(Sale.FIELD_COST, Cost.FIELD_TURNOVER);
    	
    	QueryStringBuilder queryStringBuilder = _select();
    	whereSearchCriteria(queryStringBuilder);
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy(Sale.FIELD_DATE, Boolean.TRUE).getValue();
    	registerNamedQuery(readByComputedIdentifier,_select().where(Sale.FIELD_COMPUTED_IDENTIFIER));
    	*/
    	registerNamedQuery(readAllSortedByDate,/*readByCriteriaDateAscendingOrderQuery*/_select());
        /*
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy(Sale.FIELD_DATE, Boolean.FALSE));
    	
        queryStringBuilder = _selectString(sumAttributes(attributeCost,attributeTurnover,attributeTax
        		,commonUtils.attributePath(Sale.FIELD_BALANCE, Balance.FIELD_VALUE)));
    	whereSearchCriteria(queryStringBuilder);
    	registerNamedQuery(computeByCriteria,queryStringBuilder);
    	
    	String balanceValueField = commonUtils.attributePath(Sale.FIELD_BALANCE, Balance.FIELD_VALUE);
    	
    	queryStringBuilder = _selectString(sumAttributes(attributeCost,attributeTurnover,attributeTax
        		,commonUtils.attributePath(Sale.FIELD_BALANCE, Balance.FIELD_VALUE)));
    	whereSearchCriteria(queryStringBuilder);
    	queryStringBuilder.where(LogicalOperator.AND, balanceValueField,
				commonUtils.attributePath(queryStringBuilder.getRootEntityVariableName(), Sale.FIELD_COST,Cost.FIELD_VALUE), ArithmeticOperator.EQ, Boolean.FALSE);
    	registerNamedQuery(computeByCriteriaWhereCashRegisterMovementNotExists,queryStringBuilder);
    	
    	queryStringBuilder = _selectString(sumAttributes(attributeCost,attributeTurnover,attributeTax
        		,commonUtils.attributePath(Sale.FIELD_BALANCE, Balance.FIELD_VALUE)));
    	whereSearchCriteria(queryStringBuilder);
    	queryStringBuilder.where(LogicalOperator.AND, balanceValueField,
				commonUtils.attributePath(queryStringBuilder.getRootEntityVariableName(), Sale.FIELD_COST,Cost.FIELD_VALUE), ArithmeticOperator.NEQ, Boolean.FALSE);
    	registerNamedQuery(computeByCriteriaWhereCashRegisterMovementExists,queryStringBuilder);
    	*/
    }	
	/*
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		/*queryStringBuilder.where(Sale.FIELD_COMPUTED_IDENTIFIER,ArithmeticOperator.LIKE)
		.and().between(Sale.FIELD_DATE)
		.and()
		.between(commonUtils.attributePath(Sale.FIELD_BALANCE,Balance.FIELD_VALUE), Constant.CHARACTER_COLON+PARAM_BALANCE_MIN, Constant.CHARACTER_COLON+PARAM_BALANCE_MAX)
		.and().in(commonUtils.attributePath(Sale.FIELD_FINITE_STATE_MACHINE_STATE,AbstractIdentifiable.FIELD_IDENTIFIER), PARAM_FINITE_STATE_MACHINE_STATE_IDENTIFIERS)
		;
	}*/
	
	/**/
	
	@Override
	public Collection<Sale> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sale> readByCriteria(Sale.SearchCriteria searchCriteria) {
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
	public Long countByCriteria(Sale.SearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}
	
	@Override
	public SaleResults computeByCriteria(Sale.SearchCriteria criteria) {
		QueryWrapper<?> queryWrapper = namedQuery(
				criteria.getHasAtLeastOneCashRegisterMovement()==null?computeByCriteria
						:Boolean.TRUE.equals(criteria.getHasAtLeastOneCashRegisterMovement())?
								computeByCriteriaWhereCashRegisterMovementExists:computeByCriteriaWhereCashRegisterMovementNotExists,Object.class);
		applyPeriodSearchCriteriaParameters(queryWrapper, criteria);
		
		Object[] values = (Object[]) queryWrapper.resultOne();
		SaleResults results = new SaleResults();
		results.getCost().setNumberOfProceedElements(new BigDecimal(countByCriteria(criteria)));
		results.getCost().setValue(values[0]==null?BigDecimal.ZERO:(BigDecimal) values[0]);
		results.getCost().setTurnover(values[1]==null?BigDecimal.ZERO:(BigDecimal) values[1]);
		results.getCost().setTax(values[2]==null?BigDecimal.ZERO:(BigDecimal) values[2]);
		results.setBalance(values[3]==null?BigDecimal.ZERO:(BigDecimal) values[3]);
		results.setPaid(results.getCost().getValue().subtract(results.getBalance()));
		return results;
	}
	
	/**/
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		//Sale.SearchCriteria saleSearchCriteria = (Sale.SearchCriteria) searchCriteria;
		//queryWrapper.parameterLike(Sale.FIELD_COMPUTED_IDENTIFIER,saleSearchCriteria.getIdentifierStringSearchCriteria().getPreparedValue());
		//queryWrapper.parameterIdentifiers(PARAM_FINITE_STATE_MACHINE_STATE_IDENTIFIERS, saleSearchCriteria.getFiniteStateMachineStates());
		//queryWrapper.parameterIdentifiers(saleSearchCriteria.getCustomers());
		/*BigDecimal minBalance=Constant.NUMBER_HIGHEST_NEGATIVE_LOWER_THAN_ZERO BALANCE_ZERO_MIN,maxBalance=BALANCE_ZERO_MAX;
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
		*/
	}
	
	public static final String PARAM_FINITE_STATE_MACHINE_STATE_IDENTIFIERS = "finiteStateMachineStateIdentifiers";
	public static final String PARAM_BALANCE_MIN = "minBalance";
	public static final String PARAM_BALANCE_MAX = "maxBalance";

}
