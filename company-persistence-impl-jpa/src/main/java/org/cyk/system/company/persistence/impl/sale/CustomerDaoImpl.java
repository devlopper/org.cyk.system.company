package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.root.persistence.impl.party.person.AbstractActorDaoImpl;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class CustomerDaoImpl extends AbstractActorDaoImpl<Customer> implements CustomerDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByBalance,readByBalanceGreaterThanOrEquals,readByBalanceLowerThanOrEquals,readByBalanceNotEquals;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByBalance, _select().where("balance"));
		registerNamedQuery(readByBalanceNotEquals, _select().where("balance",ArithmeticOperator.NEQ));
		registerNamedQuery(readByBalanceGreaterThanOrEquals, _select().where("balance",ArithmeticOperator.GTE));
		registerNamedQuery(readByBalanceLowerThanOrEquals, _select().where("balance",ArithmeticOperator.LTE));
	}
	
	@Override
	public Collection<Customer> readByBalance(BigDecimal balance) {
		return namedQuery(readByBalance).parameter("balance", balance).resultMany();
	}

	@Override
	public Collection<Customer> readByBalanceGreaterThanOrEquals(BigDecimal balance) {
		return namedQuery(readByBalanceGreaterThanOrEquals).parameter("balance", balance).resultMany();
	}

	@Override
	public Collection<Customer> readByBalanceLowerThanOrEquals(BigDecimal balance) {
		return namedQuery(readByBalanceLowerThanOrEquals).parameter("balance", balance).resultMany();
	}

	@Override
	public Collection<Customer> readByBalanceNotEquals(BigDecimal balance) {
		return namedQuery(readByBalanceNotEquals).parameter("balance", balance).resultMany();
	}

}
