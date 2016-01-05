package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockOutputBusiness extends AbstractSaleStockBusiness<SaleStockOutput,SaleStockOutputSearchCriteria> {

	SaleStockOutput newInstance(Person person,SaleStockInput saleStockInput);

	Collection<SaleStockOutput> findBySaleStockInput(SaleStockInput saleStockInput);
	
}
