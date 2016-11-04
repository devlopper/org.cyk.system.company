package org.cyk.system.company.business.api.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;

public interface CashRegisterMovementBusiness extends TypedBusiness<CashRegisterMovement> {

	CashRegisterMovement instanciateOne(Person person);
	CashRegisterMovement instanciateOne(CashRegister cashRegister);
	
	Collection<CashRegisterMovement> findByCashRegister(CashRegister cashRegister);
}
