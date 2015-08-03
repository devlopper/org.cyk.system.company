package org.cyk.system.company.persistence.impl.product;

import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleStockDao;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class SaleStockDaoImpl extends AbstractSaleStockDaoImpl<SaleStock,SaleStockSearchCriteria> implements SaleStockDao {

	private static final long serialVersionUID = 6920278182318788380L;

	//private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT ss FROM SaleStock ss ";
	
	/*
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = 
			"WHERE "
			+ "(EXISTS(SELECT ssi FROM SaleStockInput ssi WHERE ssi = ss AND ssi.externalIdentifier LIKE :externalIdentifier AND ssi.sale.done = :saleDone) OR "
			+ "EXISTS(SELECT sso FROM SaleStockOutput sso WHERE sso = ss AND sso.saleStockInput.externalIdentifier LIKE :externalIdentifier AND sso.saleStockInput.sale.done = :saleDone)) "
			+ "AND ss.tangibleProductStockMovement.date BETWEEN :fromDate AND :toDate AND ABS(ss.tangibleProductStockMovement.quantity) >= :minimumQuantity ";
	*/
	
	private static final String READ_BY_CRITERIA_WHERE = 
			"( EXISTS(SELECT ssi FROM SaleStockInput ssi WHERE ssi = r AND ssi.externalIdentifier LIKE :externalIdentifier AND ssi.sale.done = :saleDone) OR "
	    			+ "EXISTS(SELECT sso FROM SaleStockOutput sso WHERE sso = r AND sso.saleStockInput.externalIdentifier LIKE :externalIdentifier "
	    			+ " AND sso.saleStockInput.sale.done = :saleDone)) "
	    			+ " AND r.tangibleProductStockMovement.date BETWEEN :fromDate AND :toDate AND ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity ";
	
	//private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	//private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	/*
    	String readByCriteriaDateAscendingOrderQuery = _select().where().parenthesis(Boolean.TRUE)
    			.exists("SELECT ssi FROM SaleStockInput ssi WHERE ssi = r AND ssi.externalIdentifier LIKE :externalIdentifier AND ssi.sale.done = :saleDone")
    			.or().exists("SELECT sso FROM SaleStockOutput sso WHERE sso = r AND sso.saleStockInput.externalIdentifier LIKE :externalIdentifier "
	    			+ " AND sso.saleStockInput.sale.done = :saleDone").parenthesis(Boolean.FALSE)
    			.and().between("tangibleProductStockMovement.date").whereString(" AND ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity")
    			.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	*/
    	String readByCriteriaDateAscendingOrderQuery = _select().where()
    			.parenthesis(Boolean.TRUE)
    				.exists().openSubQueryStringBuilder(SaleStockInput.class,null,"ssi")
    					.and("externalIdentifier", ArithmeticOperator.LIKE).and("sale.done", "saleDone",ArithmeticOperator.EQ)
    					.closeSubQueryStringBuilder()	
    				.or().exists().openSubQueryStringBuilder(SaleStockOutput.class,null,"sso")
						.and("saleStockInput.externalIdentifier","externalIdentifier", ArithmeticOperator.LIKE)
						.and("saleStockInput.sale.done", "saleDone",ArithmeticOperator.EQ)
						.closeSubQueryStringBuilder()
    			.parenthesis(Boolean.FALSE)
    			.and().between("tangibleProductStockMovement.date").and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity")
    			.orderBy("tangibleProductStockMovement.date", Boolean.TRUE).getValue();
    	
    	//System.out.println("SELECT r FROM SaleStock r WHERE "+READ_BY_CRITERIA_WHERE);
    	//System.out.println(readByCriteriaDateAscendingOrderQuery);
    	
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        	
    	//registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ss.tangibleProductStockMovement.date ASC") );
    	registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
    	
    	//registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ss.tangibleProductStockMovement.date DESC") );
        
    	//registerNamedQuery(readByCriteria,_select().whereStringFormat(READ_BY_CRITERIA_WHERE_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.TRUE));
    	//registerNamedQuery(readByCriteriaDateAscendingOrder,_select().whereStringFormat(READ_BY_CRITERIA_ORDERED_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.TRUE) );
        //registerNamedQuery(readByCriteriaDateDescendingOrder,_select().whereStringFormat(READ_BY_CRITERIA_ORDERED_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.FALSE)  );
    }

}
