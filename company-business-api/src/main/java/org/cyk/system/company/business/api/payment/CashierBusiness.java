package org.cyk.system.company.business.api.payment;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;

public interface CashierBusiness extends TypedBusiness<Cashier> {

	Cashier findByPerson(Person person);
	Cashier findByCashRegister(CashRegister cashRegister);
	
}
