package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;

public interface SaleStockTangibleProductMovementInputDao extends AbstractSaleStockTangibleProductMovementDao<SaleStockTangibleProductMovementInput,SaleStockInputSearchCriteria> {

	Collection<SaleStockTangibleProductMovementInput> readBySales(Collection<Sale> sales);
	SaleStockTangibleProductMovementInput readBySale(Sale sale);
	
	//Collection<SaleStockInput> readByExternalIdentifier(String externalIdentifier);
	//Long countByExternalIdentifier(String externalIdentifier);
}
