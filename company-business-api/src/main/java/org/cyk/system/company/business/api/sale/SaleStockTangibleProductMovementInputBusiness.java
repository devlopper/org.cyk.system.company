package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockTangibleProductMovementInputBusiness extends AbstractSaleStockTangibleProductMovementBusiness<SaleStockTangibleProductMovementInput,SaleStockInputSearchCriteria> {

	SaleStockTangibleProductMovementInput instanciate(Person person);
	
	//void create(SaleStockTangibleProductMovementInput saleStock,SaleCashRegisterMovement saleCashRegisterMovement);
	
}
