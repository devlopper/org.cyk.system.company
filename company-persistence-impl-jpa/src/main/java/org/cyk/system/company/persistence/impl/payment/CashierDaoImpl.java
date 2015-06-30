package org.cyk.system.company.persistence.impl.payment;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class CashierDaoImpl extends AbstractTypedDao<Cashier> implements CashierDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByEmployee,readByPerson,readByCashRegister;

	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByEmployee, _select().where("employee"));
		registerNamedQuery(readByPerson, _select().where("employee.person","person"));
		registerNamedQuery(readByCashRegister, _select().where("cashRegister","cashRegister"));
	}

	@Override
	public Cashier readByEmployee(Employee employee) {
		return namedQuery(readByEmployee).parameter("employee", employee).ignoreThrowable(NoResultException.class).resultOne();
	}

	@Override
	public Cashier readByPerson(Person person) {
		return namedQuery(readByPerson).parameter("person", person).ignoreThrowable(NoResultException.class).resultOne();
	}

	@Override
	public Cashier readByCashRegister(CashRegister cashRegister) {
		return namedQuery(readByCashRegister).parameter("cashRegister", cashRegister).ignoreThrowable(NoResultException.class).resultOne();
	}
	
}
