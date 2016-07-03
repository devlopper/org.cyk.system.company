package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;

public class SalableProductInstanceDaoImpl extends AbstractCollectionItemDaoImpl<SalableProductInstance,SalableProduct> implements SalableProductInstanceDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readWhereNotAssociatedToCashRegister,readByCollectionByCashRegisterByFiniteStateMachineState;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readWhereNotAssociatedToCashRegister, "SELECT r FROM SalableProductInstance r WHERE NOT EXISTS("
				+ " SELECT v FROM SalableProductInstanceCashRegister v WHERE v.salableProductInstance = r "
				+ ")");
		
		registerNamedQuery(readByCollectionByCashRegisterByFiniteStateMachineState, "SELECT r FROM SalableProductInstance r WHERE r.collection = :collection AND EXISTS("
				+ " SELECT v FROM SalableProductInstanceCashRegister v WHERE v.salableProductInstance = r AND v.cashRegister = :cashRegister "
				+ "AND v.finiteStateMachineState = :finiteStateMachineState "
				+ ")");
	}
	
	@Override
	public Collection<SalableProductInstance> readWhereNotAssociatedToCashRegister() {
		return namedQuery(readWhereNotAssociatedToCashRegister).resultMany();
	}

	@Override
	public Collection<SalableProductInstance> readByCollectionByCashRegisterByFiniteStateMachineState(SalableProduct salableProduct, CashRegister cashRegister, FiniteStateMachineState finiteStateMachineState) {
		return namedQuery(readByCollectionByCashRegisterByFiniteStateMachineState).parameter(SalableProductInstance.FIELD_COLLECTION, salableProduct)
				.parameter(SalableProductInstanceCashRegister.FIELD_CASH_REGISTER, cashRegister)
				.parameter(SalableProductInstanceCashRegister.FIELD_FINITE_STATE_MACHINE_STATE, finiteStateMachineState).resultMany();
	}
	
	
}
