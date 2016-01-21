package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockOutputBusiness extends AbstractSaleStockTangibleProductMovementBusiness<SaleStockTangibleProductMovementOutput,SaleStockOutputSearchCriteria> {

	SaleStockTangibleProductMovementOutput newInstance(Person person,SaleStockTangibleProductMovementInput saleStockInput);

	Collection<SaleStockTangibleProductMovementOutput> findBySaleStockInput(SaleStockTangibleProductMovementInput saleStockInput);
	
}
