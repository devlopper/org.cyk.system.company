package org.cyk.system.company.business.api;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;

public interface BalanceBusiness {
	
	Balance computeCurrent(SaleCashRegisterMovement saleCashRegisterMovement);
	Balance computePrevious(SaleCashRegisterMovement saleCashRegisterMovement);

	Balance compute(SaleCashRegisterMovement saleCashRegisterMovement,Boolean current);
	
}
