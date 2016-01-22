package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementInputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class SaleStockTangibleProductMovementInputDaoImpl extends AbstractSaleStockTangibleProductMovementDaoImpl<SaleStockTangibleProductMovementInput,SaleStockInputSearchCriteria> implements SaleStockTangibleProductMovementInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySaleComputedIdentifier,readBySales;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	String saleDateAttribute = commonUtils.attributePath(SaleStockTangibleProductMovementInput.FIELD_SALE, Sale.FIELD_DATE);
    	/*
    	QueryStringBuilder queryStringBuilder = _select();
    	whereSearchCriteria(queryStringBuilder);
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy(saleDateAttribute, Boolean.TRUE).getValue();
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy(saleDateAttribute, Boolean.FALSE));
        */
        registerNamedQuery(readBySaleComputedIdentifier,_select().where(commonUtils.attributePath(SaleStockTangibleProductMovementInput.FIELD_SALE, Sale.FIELD_COMPUTED_IDENTIFIER),Sale.FIELD_COMPUTED_IDENTIFIER) );
        registerNamedQuery(readBySales,_select().whereIdentifierIn(SaleStockTangibleProductMovementInput.FIELD_SALE) );
        
        /*queryStringBuilder = _selectString(sumAttributes(commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_COST)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_TURNOVER)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_VALUE_ADDED_TAX)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_BALANCE,Balance.FIELD_VALUE)
        		,commonUtils.attributePath(SaleStockInput.FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT,TangibleProductStockMovement.FIELD_QUANTITY)
        		,SaleStockInput.FIELD_REMAINING_NUMBER_OF_GOODS
        		));*/
    	//whereSearchCriteria(queryStringBuilder);
    	//registerNamedQuery(computeByCriteria,queryStringBuilder);
    }
	
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		queryStringBuilder.where(SaleStockTangibleProductMovementInput.FIELD_EXTERNAL_IDENTIFIER,ArithmeticOperator.LIKE)
		.and().between(commonUtils.attributePath(SaleStockTangibleProductMovementInput.FIELD_SALE, Sale.FIELD_DATE))
		//.where(LogicalOperator.AND,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_DONE),Sale.FIELD_DONE,ArithmeticOperator.EQ)
		.and(SaleStockTangibleProductMovementInput.FIELD_REMAINING_NUMBER_OF_GOODS, PARAM_MINIMUM_REMAINING_GOODS, ArithmeticOperator.GTE)
		.and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");
	}

	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleStockInputSearchCriteria saleStockInputSearchCriteria = (SaleStockInputSearchCriteria) searchCriteria;
		
		queryWrapper.parameter(PARAM_MINIMUM_REMAINING_GOODS, saleStockInputSearchCriteria.getMinimumRemainingGoodsCount());
	}

	@Override
	public Collection<SaleStockTangibleProductMovementInput> readBySales(Collection<Sale> sales) {
		if(sales==null || sales.isEmpty())
			return new ArrayList<SaleStockTangibleProductMovementInput>();
		return namedQuery(readBySales).parameterIdentifiers(sales).resultMany();
	}

	@Override
	public SaleStockTangibleProductMovementInput readBySale(Sale sale) {
		if(sale==null)
			return null;
		Collection<SaleStockTangibleProductMovementInput> collection = readBySales(Arrays.asList(sale));
		return collection.isEmpty()?null:collection.iterator().next();
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockInputSearchCriteria criteria) {
		Object[] values = getComputeByCriteriaResults(criteria);
		SaleStocksDetails results = new SaleStocksDetails();
		results.getSaleResults().getCost().setValue(values[0]==null?BigDecimal.ZERO:(BigDecimal) values[0]);
		results.getSaleResults().getCost().setTurnover(values[1]==null?BigDecimal.ZERO:(BigDecimal) values[1]);
		results.getSaleResults().getCost().setTax(values[2]==null?BigDecimal.ZERO:(BigDecimal) values[2]);
		results.getSaleResults().setBalance(values[3]==null?BigDecimal.ZERO:(BigDecimal) values[3]);
		results.setIn(values[4]==null?BigDecimal.ZERO:(BigDecimal) values[4]);
		results.setRemaining(values[5]==null?BigDecimal.ZERO:(BigDecimal) values[5]);
		return results;
	}
	
	public static final String PARAM_MINIMUM_REMAINING_GOODS = "minimumRemainingGoods";
}
