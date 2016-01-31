package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.party.person.Person;

public interface SaleCashRegisterMovementBusiness extends TypedBusiness<SaleCashRegisterMovement> {

	SaleCashRegisterMovement instanciate(Sale sale,Person person,Boolean input);
	SaleCashRegisterMovement instanciate(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode,String amount);
	
	Collection<SaleCashRegisterMovement> findBySale(Sale sale);

	void in(SaleCashRegisterMovement saleCashRegisterMovement);
	void out(SaleCashRegisterMovement saleCashRegisterMovement);
	
	SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement,Boolean generatePos);
	
	BigDecimal computeBalance(SaleCashRegisterMovement payment);
	BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement,MovementAction movementAction,BigDecimal increment);
	
	ReportBasedOnTemplateFile<SaleReport> findReport(SaleCashRegisterMovement saleCashRegisterMovement);
	
}
