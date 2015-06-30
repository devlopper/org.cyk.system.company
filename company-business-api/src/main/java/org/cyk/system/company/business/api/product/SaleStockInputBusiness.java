package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.search.DefaultSearchCriteria;

public interface SaleStockInputBusiness extends TypedBusiness<SaleStockInput> {

	SaleStockInput newInstance(Person person);

	void create(SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement);
	
	Collection<SaleStockInput> findByCriteria(DefaultSearchCriteria criteria);
	Long countByCriteria(DefaultSearchCriteria criteria);
	
}
