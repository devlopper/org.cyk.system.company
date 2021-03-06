package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

public interface SalableProductInstanceCashRegisterBusiness extends TypedBusiness<SalableProductInstanceCashRegister> {

	Collection<SalableProductInstanceCashRegister> findByCriteria(SearchCriteria searchCriteria);
	Long countByCriteria(SearchCriteria criteria);
	
	SalableProductInstanceCashRegister findBySalableProductInstanceByCashRegister(SalableProductInstance salableProductInstance, CashRegister cashRegister);
	
	Collection<SalableProductInstanceCashRegister> create(Collection<String> salableInstanceCodes,CashRegister cashRegister,FiniteStateMachineState finiteStateMachineState);
	Collection<SalableProductInstanceCashRegister> create(Collection<String> salableInstanceCodes,CashRegister cashRegister);
}
