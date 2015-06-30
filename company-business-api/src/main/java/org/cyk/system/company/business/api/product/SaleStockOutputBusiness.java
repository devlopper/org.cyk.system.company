package org.cyk.system.company.business.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.search.DefaultSearchCriteria;

public interface SaleStockOutputBusiness extends TypedBusiness<SaleStockOutput> {

	SaleStockOutput newInstance(Person person,SaleStockInput saleStockInput);

	Collection<SaleStockOutput> findByCriteria(DefaultSearchCriteria criteria);
	Long countByCriteria(DefaultSearchCriteria criteria);

	Collection<SaleStockOutput> findBySaleStockInput(SaleStockInput saleStockInput);
	
}
