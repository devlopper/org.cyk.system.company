package org.cyk.system.company.persistence.impl.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

@Deprecated
public class CashRegisterDaoImpl extends AbstractEnumerationDaoImpl<CashRegister> implements CashRegisterDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByPerson;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByPerson, "SELECT r FROM CashRegister r WHERE EXISTS("
				+ " SELECT v FROM Cashier v WHERE v.cashRegister = r AND v.person = :person"
				+ ")");
	}
	
	@Override
	public Collection<CashRegister> readByPerson(Person person) {
		return namedQuery(readByPerson).parameter(Cashier.FIELD_PERSON, person).resultMany();
	}

	
}
