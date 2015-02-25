package org.cyk.system.company.business.api;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Sale;

public interface CompanyValueGenerator {

	String saleIdentificationNumber(Sale sale);
	
	String cashRegisterMovementIdentificationNumber(CashRegisterMovement cashRegisterMovement);
	
}
