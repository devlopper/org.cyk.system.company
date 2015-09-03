package org.cyk.system.company.persistence.impl.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.product.SaleStocksDetails;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.computation.LogicalOperator;

public class SaleStockInputDaoImpl extends AbstractSaleStockDaoImpl<SaleStockInput,SaleStockInputSearchCriteria> implements SaleStockInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySaleComputedIdentifier,readBySales;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	String saleDateAttribute = commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_DATE);
    	
    	QueryStringBuilder queryStringBuilder = _select();
    	whereSearchCriteria(queryStringBuilder);
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy(saleDateAttribute, Boolean.TRUE).getValue();
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy(saleDateAttribute, Boolean.FALSE));
        
        registerNamedQuery(readBySaleComputedIdentifier,_select().where(commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_COMPUTED_IDENTIFIER),Sale.FIELD_COMPUTED_IDENTIFIER) );
        registerNamedQuery(readBySales,_select().whereIdentifierIn(SaleStockInput.FIELD_SALE) );
        
        queryStringBuilder = _selectString(sumAttributes(commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_COST)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_TURNOVER)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_VALUE_ADDED_TAX)
        		,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_BALANCE,Balance.FIELD_VALUE)
        		,commonUtils.attributePath(SaleStockInput.FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT,TangibleProductStockMovement.FIELD_QUANTITY)
        		,SaleStockInput.FIELD_REMAINING_NUMBER_OF_GOODS
        		));
    	whereSearchCriteria(queryStringBuilder);
    	registerNamedQuery(computeByCriteria,queryStringBuilder);
    }
	
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		queryStringBuilder.where(SaleStockInput.FIELD_EXTERNAL_IDENTIFIER,ArithmeticOperator.LIKE)
		.and().between(commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_DATE))
		.where(LogicalOperator.AND,commonUtils.attributePath(SaleStockInput.FIELD_SALE, Sale.FIELD_DONE),Sale.FIELD_DONE,ArithmeticOperator.EQ)
		.and(SaleStockInput.FIELD_REMAINING_NUMBER_OF_GOODS, PARAM_MINIMUM_REMAINING_GOODS, ArithmeticOperator.GTE)
		.and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");
	}

	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleStockInputSearchCriteria saleStockInputSearchCriteria = (SaleStockInputSearchCriteria) searchCriteria;
		
		queryWrapper.parameter(PARAM_MINIMUM_REMAINING_GOODS, saleStockInputSearchCriteria.getMinimumRemainingGoodsCount());
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

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockInputSearchCriteria criteria) {
		Object[] values = getComputeByCriteriaResults(criteria);
		SaleStocksDetails results = new SaleStocksDetails();
		results.getSalesDetails().setCost((BigDecimal) values[0]);
		results.getSalesDetails().setTurnover((BigDecimal) values[1]);
		results.getSalesDetails().setValueAddedTax((BigDecimal) values[2]);
		results.getSalesDetails().setBalance((BigDecimal) values[3]);
		results.setIn((BigDecimal) values[4]);
		results.setRemaining((BigDecimal) values[5]);
		return results;
	}
	
	public static final String PARAM_MINIMUM_REMAINING_GOODS = "minimumRemainingGoods";
}
