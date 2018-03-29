package org.cyk.system.company.business.api.sale;

import java.util.Collection;
import java.util.Set;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

@Deprecated
public interface SalableProductInstanceBusiness extends AbstractEnumerationBusiness<SalableProductInstance> {

	void create(SalableProduct salableProduct,Set<String> codes);
	
	Collection<SalableProductInstance> findWhereNotAssociatedToCashRegister();

	Collection<SalableProductInstance> findByCollectionByCashRegisterByFiniteStateMachineState(SalableProduct salableProduct, CashRegister cashRegister, FiniteStateMachineState finiteStateMachineState);
}
