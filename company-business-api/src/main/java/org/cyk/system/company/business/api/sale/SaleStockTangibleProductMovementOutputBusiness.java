package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface SaleStockTangibleProductMovementOutputBusiness extends AbstractSaleStockTangibleProductMovementBusiness<SaleStockTangibleProductMovementOutput,SaleStockOutputSearchCriteria> {

	SaleStockTangibleProductMovementOutput instanciateOne(Person person,SaleStockTangibleProductMovementInput input);

	Collection<SaleStockTangibleProductMovementOutput> findBySaleStockInput(SaleStockTangibleProductMovementInput input);
	
}
