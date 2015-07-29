package org.cyk.system.company.persistence.impl.product;

import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleStockDao;

public class SaleStockDaoImpl extends AbstractSaleStockDaoImpl<SaleStock,SaleStockSearchCriteria> implements SaleStockDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT ss FROM SaleStock ss ";
	
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = 
			"WHERE "
			+ "(EXISTS(SELECT ssi FROM SaleStockInput ssi WHERE ssi = ss AND ssi.externalIdentifier LIKE :externalIdentifier AND ssi.sale.done = :saleDone) OR "
			+ "EXISTS(SELECT sso FROM SaleStockOutput sso WHERE sso = ss AND sso.saleStockInput.externalIdentifier LIKE :externalIdentifier AND sso.saleStockInput.sale.done = :saleDone)) "
			+ "AND ss.tangibleProductStockMovement.date BETWEEN :fromDate AND :toDate AND ABS(ss.tangibleProductStockMovement.quantity) >= :minimumQuantity ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY ss.tangibleProductStockMovement.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ss.tangibleProductStockMovement.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "ss.tangibleProductStockMovement.date DESC") );
        
    	//registerNamedQuery(readByCriteria,_select().whereStringFormat(READ_BY_CRITERIA_WHERE_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.TRUE));
    	//registerNamedQuery(readByCriteriaDateAscendingOrder,_select().whereStringFormat(READ_BY_CRITERIA_ORDERED_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.TRUE) );
        //registerNamedQuery(readByCriteriaDateDescendingOrder,_select().whereStringFormat(READ_BY_CRITERIA_ORDERED_FORMAT).orderBy("tangibleProductStockMovement.date", Boolean.FALSE)  );
    }

}
