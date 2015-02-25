package org.cyk.system.company.persistence.impl.payment;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class CashRegisterDaoImpl extends AbstractTypedDao<CashRegister> implements CashRegisterDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String sumBalance;

	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(sumBalance, "SELECT SUM(cr.balance) FROM CashRegister cr");
	}

	@Override
	public BigDecimal sumBalance() { 
		return namedQuery(sumBalance,BigDecimal.class).nullValue(BigDecimal.ZERO).resultOne();
	}
}
