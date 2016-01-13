package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;

public interface SaleStockInputDao extends AbstractSaleStockDao<SaleStockInput,SaleStockInputSearchCriteria> {

	Collection<SaleStockInput> readBySales(Collection<Sale> sales);
	SaleStockInput readBySale(Sale sale);
	
	//Collection<SaleStockInput> readByExternalIdentifier(String externalIdentifier);
	//Long countByExternalIdentifier(String externalIdentifier);
}