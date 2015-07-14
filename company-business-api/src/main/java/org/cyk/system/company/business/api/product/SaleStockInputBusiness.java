package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockInputBusiness extends TypedBusiness<SaleStockInput> {

	SaleStockInput newInstance(Person person);

	void create(SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement);
	
	Collection<SaleStockInput> findByCriteria(SaleStockInputSearchCriteria criteria);
	Long countByCriteria(SaleStockInputSearchCriteria criteria);
	
}
