package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.model.sale.SaleStock;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockDao;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class SaleStockDaoImpl extends AbstractSaleStockDaoImpl<SaleStock,SaleStockSearchCriteria> implements SaleStockDao {

	private static final long serialVersionUID = 6920278182318788380L;

	@Inject private SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	QueryStringBuilder queryStringBuilder = _select();/*.where()
    			.parenthesis(Boolean.TRUE)
				.exists().openSubQueryStringBuilder(SaleStockInput.class,null,"ssi")
					.and("externalIdentifier", ArithmeticOperator.LIKE).and("sale.done", "done",ArithmeticOperator.EQ)
					.closeSubQueryStringBuilder()	
				.or().exists().openSubQueryStringBuilder(SaleStockOutput.class,null,"sso")
					.and("saleStockInput.externalIdentifier","externalIdentifier", ArithmeticOperator.LIKE)
					.and("saleStockInput.sale.done", "done",ArithmeticOperator.EQ)
					.closeSubQueryStringBuilder()
			.parenthesis(Boolean.FALSE)
			.and().between("tangibleProductStockMovement.date").and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");*/
    	whereSearchCriteria(queryStringBuilder);
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);	
    	registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
    	registerNamedQuery(readByCriteriaDateDescendingOrder,
    			queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.FALSE));
    	
    	queryStringBuilder = _selectString(
    		//" SUM(CASE WHEN TYPE(r)='SaleStockInput' THEN r.sale.cost ELSE r.saleStockInput.sale.cost END) AS AMOUNT,"
    		//+ " SUM(CASE WHEN TYPE(r)='SaleStockInput' THEN r.sale. ELSE r.saleStockInput.sale.cost END) AS AMOUNT,"
    		" SUM(CASE WHEN r.tangibleProductStockMovement.quantity > 0 THEN r.tangibleProductStockMovement.quantity ELSE 0 END) AS STOCK_IN,"
    	    + " SUM(CASE WHEN r.tangibleProductStockMovement.quantity < 0 THEN ABS(r.tangibleProductStockMovement.quantity) ELSE 0 END) AS STOCK_OUT "
    	);
    	whereSearchCriteria(queryStringBuilder);
    	registerNamedQuery(computeByCriteria,queryStringBuilder);
    	
    }
	
	private void whereSearchCriteria(QueryStringBuilder queryStringBuilder){
		queryStringBuilder.where()
			.parenthesis(Boolean.TRUE)
			.exists().openSubQueryStringBuilder(SaleStockInput.class,null,"ssi")
				.and("externalIdentifier", ArithmeticOperator.LIKE).and("sale.done", "done",ArithmeticOperator.EQ)
				.closeSubQueryStringBuilder()	
			.or().exists().openSubQueryStringBuilder(SaleStockOutput.class,null,"sso")
				.and("saleStockInput.externalIdentifier","externalIdentifier", ArithmeticOperator.LIKE)
				.and("saleStockInput.sale.done", "done",ArithmeticOperator.EQ)
				.closeSubQueryStringBuilder()
		.parenthesis(Boolean.FALSE)
		.and().between("tangibleProductStockMovement.date").and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockSearchCriteria criteria) {
		Object[] values = getComputeByCriteriaResults(criteria);
		SaleStocksDetails results = new SaleStocksDetails();
		
		results.setIn(values[0]==null?BigDecimal.ZERO:(BigDecimal) values[0]);
		results.setOut(values[1]==null?BigDecimal.ZERO:(BigDecimal) values[1]);
		results.setRemaining(results.getIn().subtract(results.getOut()));
		results.setSalesDetails(saleDao.computeByCriteria(criteria.getSaleSearchCriteria()));
		
		return results;
	}

}
