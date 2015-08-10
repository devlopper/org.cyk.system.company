package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.computation.LogicalOperator;

public class SaleStockOutputDaoImpl extends AbstractSaleStockDaoImpl<SaleStockOutput,SaleStockOutputSearchCriteria> implements SaleStockOutputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySaleStockInput;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	
    	QueryStringBuilder queryStringBuilder = _select().where("saleStockInput.externalIdentifier","externalIdentifier",ArithmeticOperator.LIKE)
    			.and().between("tangibleProductStockMovement.date").where(LogicalOperator.AND,"saleStockInput.sale.done","saleDone",ArithmeticOperator.EQ)
    			.and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity")
    			.and().whereString("r.saleCashRegisterMovement.cashRegisterMovement.amount >= :minimumPaid");
        	
        String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	
        registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy("tangibleProductStockMovement.date", Boolean.FALSE));	
        
        registerNamedQuery(readBySaleStockInput,_select().where("saleStockInput"));
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
	
}
