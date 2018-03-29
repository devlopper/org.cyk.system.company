package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

@Deprecated
public class SalableProductInstanceDaoImpl extends AbstractEnumerationDaoImpl<SalableProductInstance> implements SalableProductInstanceDao {
	private static final long serialVersionUID = 6920278182318788380L;

	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		
	}
	
	@Override
	public Collection<SalableProductInstance> readWhereNotAssociatedToCashRegister() {
		return null;
	}

	@Override
	public Collection<SalableProductInstance> readByCollectionByCashRegisterByFiniteStateMachineState(SalableProduct salableProduct, CashRegister cashRegister, FiniteStateMachineState finiteStateMachineState) {
		return null;
	}
	
	
}
