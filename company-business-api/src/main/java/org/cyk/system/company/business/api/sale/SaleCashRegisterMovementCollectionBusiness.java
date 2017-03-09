package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;
import org.cyk.system.root.model.security.UserAccount;

public interface SaleCashRegisterMovementCollectionBusiness extends AbstractCollectionBusiness<SaleCashRegisterMovementCollection,SaleCashRegisterMovement> {

	SaleCashRegisterMovementCollection instanciateOne(String code, String name, String cashRegisterCode,Object[][] saleCashRegisterMovements);
	void setCashRegister(UserAccount userAccount,SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,CashRegister cashRegister);
	
	void computeAmount(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,Collection<SaleCashRegisterMovement> saleCashRegisterMovements);
	
	/*
	 * Amount setters
	void in(SaleCashRegisterMovement saleCashRegisterMovement);
	void out(SaleCashRegisterMovement saleCashRegisterMovement);
	*/
}
