package org.cyk.system.company.persistence.api.payment;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.TypedDao;

public interface CashierDao extends TypedDao<Cashier> {

	Cashier readByPerson(Person person);
	Cashier readByCashRegister(CashRegister cashRegister);
}
