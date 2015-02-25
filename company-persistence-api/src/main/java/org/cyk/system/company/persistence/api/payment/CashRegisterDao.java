package org.cyk.system.company.persistence.api.payment;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.persistence.api.TypedDao;

public interface CashRegisterDao extends TypedDao<CashRegister> {

	BigDecimal sumBalance();

}
