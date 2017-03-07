package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.AbstractCollectionItemBusiness;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.security.UserAccount;

public interface SaleCashRegisterMovementBusiness extends AbstractCollectionItemBusiness<SaleCashRegisterMovement,SaleCashRegisterMovementCollection> {

	SaleCashRegisterMovement instanciateOne(Sale sale,CashRegister cashRegister,Boolean input);
	SaleCashRegisterMovement instanciateOne(String collectionCode,String saleCode, String amount);
	SaleCashRegisterMovement instanciateOne(SaleCashRegisterMovementCollection collection,String saleCode, String amount);
	SaleCashRegisterMovement instanciateOne(UserAccount userAccount, Sale sale,CashRegister cashRegister);
	
	Collection<SaleCashRegisterMovement> findBySale(Sale sale);

	BigDecimal computeBalance(SaleCashRegisterMovement payment);
	BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement,MovementAction movementAction,BigDecimal increment);
	
	void setSale(SaleCashRegisterMovement saleCashRegisterMovement,Sale sale);
	
}
