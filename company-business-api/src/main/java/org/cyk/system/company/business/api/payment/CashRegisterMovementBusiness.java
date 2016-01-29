package org.cyk.system.company.business.api.payment;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;

public interface CashRegisterMovementBusiness extends TypedBusiness<CashRegisterMovement> {

	CashRegisterMovement instanciate(Person person);
}
