package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleCashRegisterMovementBusiness extends TypedBusiness<SaleCashRegisterMovement> {

	Collection<SaleCashRegisterMovement> findBySale(Sale sale);

	/*
	void in(SaleCashRegisterMovement payment);
	void out(SaleCashRegisterMovement payment);
	*/
	
	//SaleCashRegisterMovement create(SaleCashRegisterMovement payment,Boolean payback);
	
	BigDecimal computeBalance(SaleCashRegisterMovement payment);
}
