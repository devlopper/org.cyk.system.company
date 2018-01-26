package org.cyk.system.company.persistence.api.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.persistence.api.TypedDao;

@Deprecated
public interface CashRegisterMovementDao extends TypedDao<CashRegisterMovement> {

	Collection<CashRegisterMovement> readByCashRegister(CashRegister cashRegister);


}
