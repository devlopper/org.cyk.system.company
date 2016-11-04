package org.cyk.system.company.persistence.impl.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class CashRegisterMovementDaoImpl extends AbstractTypedDao<CashRegisterMovement> implements CashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByCashRegister;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCashRegister, _select().where(CashRegisterMovement.FIELD_CASH_REGISTER));
	}
	
	@Override
	public Collection<CashRegisterMovement> readByCashRegister(CashRegister cashRegister) {
		return namedQuery(readByCashRegister).parameter(CashRegisterMovement.FIELD_CASH_REGISTER, cashRegister).resultMany();
	}

	

}
