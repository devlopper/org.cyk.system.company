package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleStockTangibleProductMovementDaoImpl extends AbstractTypedDao<SaleStockTangibleProductMovement> implements SaleStockTangibleProductMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;
	
	private String readBySale;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	registerNamedQuery(readBySale, _select().where(SaleStockTangibleProductMovement.FIELD_SALE));
    }

	@Override
	public Collection<SaleStockTangibleProductMovement> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter(SaleStockTangibleProductMovement.FIELD_SALE, sale).resultMany();
	}
	
	

}
