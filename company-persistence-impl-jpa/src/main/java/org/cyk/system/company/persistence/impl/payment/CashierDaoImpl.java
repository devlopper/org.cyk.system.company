package org.cyk.system.company.persistence.impl.payment;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class CashierDaoImpl extends AbstractTypedDao<Cashier> implements CashierDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByPerson,readByCashRegister;

	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByPerson, _select().where(Cashier.FIELD_PERSON));
		registerNamedQuery(readByCashRegister, _select().where(Cashier.FIELD_CASH_REGISTER));
	}

	@Override
	public Cashier readByPerson(Person person) {
		return namedQuery(readByPerson).parameter(Cashier.FIELD_PERSON, person).ignoreThrowable(NoResultException.class).resultOne();
	}

	@Override
	public Cashier readByCashRegister(CashRegister cashRegister) {
		return namedQuery(readByCashRegister).parameter(Cashier.FIELD_CASH_REGISTER, cashRegister).ignoreThrowable(NoResultException.class).resultOne();
	}
	
}
