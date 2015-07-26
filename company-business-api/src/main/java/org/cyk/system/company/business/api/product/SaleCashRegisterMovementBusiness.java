package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;

public interface SaleCashRegisterMovementBusiness extends TypedBusiness<SaleCashRegisterMovement> {

	SaleCashRegisterMovement newInstance(Sale sale,Person person);
	
	Collection<SaleCashRegisterMovement> findBySale(Sale sale);

	void in(SaleCashRegisterMovement saleCashRegisterMovement);
	void out(SaleCashRegisterMovement saleCashRegisterMovement);
	
	
	//SaleCashRegisterMovement create(SaleCashRegisterMovement payment,Boolean payback);
	
	BigDecimal computeBalance(SaleCashRegisterMovement payment);
	
}
