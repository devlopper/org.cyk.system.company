package org.cyk.system.company.business.api.payment;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.business.api.TypedBusiness;

public interface CashRegisterBusiness extends TypedBusiness<CashRegister> {

	BigDecimal sumBalance();
	
}
