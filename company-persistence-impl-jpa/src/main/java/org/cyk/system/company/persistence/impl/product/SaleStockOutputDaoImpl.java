package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SaleStockOutputDaoImpl extends AbstractSaleStockDaoImpl<SaleStockOutput,SaleStockOutputSearchCriteria> implements SaleStockOutputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private static final String READ_BY_CRITERIA_SELECT_FORMAT = "SELECT sso FROM SaleStockOutput sso ";
	private static final String READ_BY_CRITERIA_WHERE_FORMAT = "WHERE sso.tangibleProductStockMovement.date BETWEEN :fromvalue AND :tovalue"
			+ " AND sso.saleCashRegisterMovement.cashRegisterMovement.amount >= :minimumPaid "
			+ "AND ABS(sso.tangibleProductStockMovement.quantity) >= :minimumQuantity AND sso.saleStockInput.externalIdentifier LIKE :externalIdentifier"
			+ " AND sso.saleStockInput.sale.done = :saleDone ";
	
	private static final String READ_BY_CRITERIA_NOTORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT;
	private static final String READ_BY_CRITERIA_ORDERED_FORMAT = READ_BY_CRITERIA_SELECT_FORMAT+READ_BY_CRITERIA_WHERE_FORMAT+ORDER_BY_FORMAT;
	
	private String readBySaleStockInput;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readByCriteria,READ_BY_CRITERIA_NOTORDERED_FORMAT+" ORDER BY sso.tangibleProductStockMovement.date ASC");
        registerNamedQuery(readByCriteriaDateAscendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sso.tangibleProductStockMovement.date ASC") );
        registerNamedQuery(readByCriteriaDateDescendingOrder,String.format(READ_BY_CRITERIA_ORDERED_FORMAT, "sso.tangibleProductStockMovement.date DESC") );
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
