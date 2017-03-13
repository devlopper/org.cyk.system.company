package org.cyk.system.company.business.api.payment;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.security.UserAccount;

public interface CashRegisterMovementBusiness extends TypedBusiness<CashRegisterMovement> {

	CashRegisterMovement instanciateOne(UserAccount userAccount,CashRegister cashRegister);
	CashRegisterMovement instanciateOne(String code, String name, String amount, String cashRegisterCode,String cashRegisterMovementModeCode);
	CashRegisterMovement instanciateOne(String code, String name, String amount, String cashRegisterCode);
	
	void setCashRegister(CashRegisterMovement cashRegisterMovement,CashRegister cashRegister);
	Collection<CashRegisterMovement> findByCashRegister(CashRegister cashRegister);
	
	/**/
	
	//TODO use global script to generate code
	String RUNTIME_CODE_GENERATOR_IDENTIFIER = "CASH_REGISTER_MOVEMENT_RUNTIME_CODE_GENERATOR_IDENTIFIER";//TODO can use script concept

}
