package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.security.UserAccount;

public interface SaleCashRegisterMovementBusiness extends TypedBusiness<SaleCashRegisterMovement> {

	SaleCashRegisterMovement instanciateOne(Sale sale,CashRegister cashRegister,Boolean input);
	SaleCashRegisterMovement instanciateOne(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode,String amount);
	SaleCashRegisterMovement instanciateOne(UserAccount userAccount, Sale sale,CashRegister cashRegister);
	
	Collection<SaleCashRegisterMovement> findBySale(Sale sale);

	void in(SaleCashRegisterMovement saleCashRegisterMovement);
	void out(SaleCashRegisterMovement saleCashRegisterMovement);
	
	BigDecimal computeBalance(SaleCashRegisterMovement payment);
	BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement,MovementAction movementAction,BigDecimal increment);
	
	ReportBasedOnTemplateFile<SaleReport> findReport(SaleCashRegisterMovement saleCashRegisterMovement);
	
	void setSale(SaleCashRegisterMovement saleCashRegisterMovement,Sale sale);
	void setCashRegister(UserAccount userAccount,SaleCashRegisterMovement saleCashRegisterMovement,CashRegister cashRegister);
}
