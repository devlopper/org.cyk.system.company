package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

@Deprecated
public interface SalableProductInstanceDao extends AbstractEnumerationDao<SalableProductInstance> {
	
	Collection<SalableProductInstance> readWhereNotAssociatedToCashRegister();

	Collection<SalableProductInstance> readByCollectionByCashRegisterByFiniteStateMachineState(SalableProduct salableProduct, CashRegister cashRegister, FiniteStateMachineState finiteStateMachineState);
	
}
