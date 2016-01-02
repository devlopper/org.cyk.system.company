package org.cyk.system.company.persistence.api.payment;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface CashRegisterDao extends AbstractEnumerationDao<CashRegister> {

	BigDecimal sumBalance();
 
}
