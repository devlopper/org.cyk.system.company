package org.cyk.system.company.business.api.product;

import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockInputBusiness extends AbstractSaleStockBusiness<SaleStockInput,SaleStockInputSearchCriteria> {

	SaleStockInput newInstance(Person person);
	
	void create(SaleStockInput saleStock,SaleCashRegisterMovement saleCashRegisterMovement);
	
}
