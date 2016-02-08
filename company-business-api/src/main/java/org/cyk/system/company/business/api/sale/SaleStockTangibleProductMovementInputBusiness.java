package org.cyk.system.company.business.api.sale;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;

public interface SaleStockTangibleProductMovementInputBusiness extends AbstractSaleStockTangibleProductMovementBusiness<SaleStockTangibleProductMovementInput,SaleStockInputSearchCriteria> {

	SaleStockTangibleProductMovementInput instanciateOne(Sale sale);
	
	//void create(SaleStockTangibleProductMovementInput saleStock,SaleCashRegisterMovement saleCashRegisterMovement);
	
}
