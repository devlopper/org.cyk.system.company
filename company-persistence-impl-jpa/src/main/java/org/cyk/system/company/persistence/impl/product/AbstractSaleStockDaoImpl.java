package org.cyk.system.company.persistence.impl.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.AbstractSaleStockDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public abstract class AbstractSaleStockDaoImpl<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractPeriodSearchCriteria> extends AbstractTypedDao<SALE_STOCK> implements AbstractSaleStockDao<SALE_STOCK,SEARCH_CRITERIA> {

	private static final long serialVersionUID = 6920278182318788380L;

	protected String readAllSortedByDate,readByTangibleProductStockMovements;
	
	@Inject protected SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readAllSortedByDate,"SELECT ss FROM SaleStock ss ORDER BY ss.tangibleProductStockMovement.date ASC");
    	registerNamedQuery(readByTangibleProductStockMovements,_select().whereIdentifierIn("tangibleProductStockMovement") );
    }
	
	@Override
	public Collection<SALE_STOCK> readAll() {
		return namedQuery(readAllSortedByDate).resultMany();
	}
	
	@Override
	public Collection<SALE_STOCK> readByTangibleProductStockMovements(Collection<TangibleProductStockMovement> tangibleProductStockMovements) {
		if(tangibleProductStockMovements==null || tangibleProductStockMovements.isEmpty())
			return new ArrayList<SALE_STOCK>();
		return namedQuery(readByTangibleProductStockMovements).parameterIdentifiers(tangibleProductStockMovements).resultMany();
	}

	@Override
	public SALE_STOCK readByTangibleProductStockMovement(TangibleProductStockMovement tangibleProductStockMovement) {
		if(tangibleProductStockMovement==null)
			return null;
		Collection<SALE_STOCK> collection = readByTangibleProductStockMovements(Arrays.asList(tangibleProductStockMovement));
		return collection.isEmpty()?null:collection.iterator().next();
	}

	
}
