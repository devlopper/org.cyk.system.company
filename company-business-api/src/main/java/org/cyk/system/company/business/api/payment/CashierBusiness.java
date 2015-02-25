package org.cyk.system.company.business.api.payment;

import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.TypedBusiness;

public interface CashierBusiness extends TypedBusiness<Cashier> {

	Cashier findByEmployee(Employee employee);
	
}
