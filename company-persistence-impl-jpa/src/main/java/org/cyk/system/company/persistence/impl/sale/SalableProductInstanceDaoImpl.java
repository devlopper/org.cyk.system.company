package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;

public class SalableProductInstanceDaoImpl extends AbstractCollectionItemDaoImpl<SalableProductInstance,SalableProduct> implements SalableProductInstanceDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readWhereNotAssociatedToCashRegister;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readWhereNotAssociatedToCashRegister, "SELECT r FROM SalableProductInstance r WHERE NOT EXISTS("
				+ " SELECT v FROM SalableProductInstanceCashRegister v WHERE v.salableProductInstance = r "
				+ ")");
	}
	
	@Override
	public Collection<SalableProductInstance> readWhereNotAssociatedToCashRegister() {
		return namedQuery(readWhereNotAssociatedToCashRegister).resultMany();
	}
}
