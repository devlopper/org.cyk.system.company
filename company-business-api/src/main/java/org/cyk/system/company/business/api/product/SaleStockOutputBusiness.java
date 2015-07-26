package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockOutputBusiness extends AbstractSaleStockBusiness<SaleStockOutput,SaleStockOutputSearchCriteria> {

	SaleStockOutput newInstance(Person person,SaleStockInput saleStockInput);

	Collection<SaleStockOutput> findBySaleStockInput(SaleStockInput saleStockInput);
	
}
