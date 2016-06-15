package org.cyk.system.company.persistence.api.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface CashRegisterDao extends AbstractEnumerationDao<CashRegister> {

	Collection<CashRegister> readByPerson(Person person);

	
}
