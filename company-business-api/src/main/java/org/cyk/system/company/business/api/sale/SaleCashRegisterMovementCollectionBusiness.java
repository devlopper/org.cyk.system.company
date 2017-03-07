package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;
import org.cyk.system.root.model.security.UserAccount;

public interface SaleCashRegisterMovementCollectionBusiness extends AbstractCollectionBusiness<SaleCashRegisterMovementCollection,SaleCashRegisterMovement> {

	SaleCashRegisterMovementCollection instanciateOne(String code, String name, String cashRegisterCode);
	void setCashRegister(UserAccount userAccount,SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,CashRegister cashRegister);
	/*
	 * Amount setters
	void in(SaleCashRegisterMovement saleCashRegisterMovement);
	void out(SaleCashRegisterMovement saleCashRegisterMovement);
	*/
}
