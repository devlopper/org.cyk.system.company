package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockInputBusiness extends AbstractSaleStockBusiness<SaleStockInput,SaleStockInputSearchCriteria> {

	SaleStockInput newInstance(Person person);
	
	void create(SaleStockInput saleStock,SaleCashRegisterMovement saleCashRegisterMovement);
	
	void complete(SaleStockInput saleStock,SaleCashRegisterMovement saleCashRegisterMovement);
}
