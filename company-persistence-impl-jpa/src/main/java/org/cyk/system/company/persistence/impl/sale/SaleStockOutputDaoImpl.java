package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.sale.SalesDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockOutputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.computation.LogicalOperator;

public class SaleStockOutputDaoImpl extends AbstractSaleStockDaoImpl<SaleStockOutput,SaleStockOutputSearchCriteria> implements SaleStockOutputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	@Inject private SaleDao saleDao;
	private String readBySaleStockInput;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	
    	QueryStringBuilder queryStringBuilder = _select();
    	whereSearchCriteria(queryStringBuilder);
    		
        String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	
        registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.FALSE));	
        
        registerNamedQuery(readBySaleStockInput,_select().where("saleStockInput"));
        
        /*queryStringBuilder = _selectString(sumAttributes(
        		commonUtils.attributePath(SaleStockOutput.FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT, TangibleProductStockMovement.FIELD_QUANTITY)
        		,commonUtils.attributePath(SaleStockOutput.FIELD_SALE_CASH_REGISTER_MOVEMENT, SaleCashRegisterMovement.FIELD_CASH_REGISTER_MOVEMENT,CashRegisterMovement.FIELD_AMOUNT)
        		
        		));
    	whereSearchCriteria(queryStringBuilder);
    	registerNamedQuery(computeByCriteria,queryStringBuilder);
    	*/
    }
	
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		queryStringBuilder.where("saleStockInput.externalIdentifier","externalIdentifier",ArithmeticOperator.LIKE)
		.and().between("tangibleProductStockMovement.date")
		//.where(LogicalOperator.AND,"saleStockInput.sale.done","done",ArithmeticOperator.EQ)
		.and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity")
		.and().whereString("r.saleCashRegisterMovement.cashRegisterMovement.amount >= :minimumPaid");
	}
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		SaleStockOutputSearchCriteria saleStockOutputSearchCriteria = (SaleStockOutputSearchCriteria) searchCriteria;
		queryWrapper.parameter("minimumPaid", saleStockOutputSearchCriteria.getMinimumPaid());
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
	}

	@Override
	public Collection<SaleStockOutput> readBySaleStockInput(SaleStockInput saleStockInput) {
		return namedQuery(readBySaleStockInput).parameter("saleStockInput", saleStockInput).resultMany();
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockOutputSearchCriteria criteria) {
		Object[] values = getComputeByCriteriaResults(criteria);
		SaleStocksDetails results = new SaleStocksDetails();
		results.setOut(values[0]==null?BigDecimal.ZERO:(BigDecimal) values[0]);
		results.getSalesDetails().setPaid(values[1]==null?BigDecimal.ZERO:(BigDecimal) values[1]);
		//SalesDetails salesDetails = saleDao.computeByCriteria(criteria.getSaleSearchCriteria());
		//results.getSalesDetails().setBalance(salesDetails.getBalance());
		return results;
	}
	
}
