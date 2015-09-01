package org.cyk.system.company.persistence.impl.product;

import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStocksDetails;
import org.cyk.system.company.persistence.api.product.SaleStockDao;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class SaleStockDaoImpl extends AbstractSaleStockDaoImpl<SaleStock,SaleStockSearchCriteria> implements SaleStockDao {

	private static final long serialVersionUID = 6920278182318788380L;

	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	QueryStringBuilder queryStringBuilder = _select().where()
    			.parenthesis(Boolean.TRUE)
				.exists().openSubQueryStringBuilder(SaleStockInput.class,null,"ssi")
					.and("externalIdentifier", ArithmeticOperator.LIKE).and("sale.done", "saleDone",ArithmeticOperator.EQ)
					.closeSubQueryStringBuilder()	
				.or().exists().openSubQueryStringBuilder(SaleStockOutput.class,null,"sso")
					.and("saleStockInput.externalIdentifier","externalIdentifier", ArithmeticOperator.LIKE)
					.and("saleStockInput.sale.done", "saleDone",ArithmeticOperator.EQ)
					.closeSubQueryStringBuilder()
			.parenthesis(Boolean.FALSE)
			.and().between("tangibleProductStockMovement.date").and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);	
    	registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
    	registerNamedQuery(readByCriteriaDateDescendingOrder,
    			queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.FALSE));
    	
    }

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockSearchCriteria criteria) {
		return null;
	}

}
