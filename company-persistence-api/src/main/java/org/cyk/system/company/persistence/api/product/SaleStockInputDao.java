package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.Sale;

public interface SaleStockInputDao extends AbstractSaleStockDao<SaleStockInput,SaleStockInputSearchCriteria> {

	Collection<SaleStockInput> readBySales(Collection<Sale> sales);
	SaleStockInput readBySale(Sale sale);
	
	//Collection<SaleStockInput> readByExternalIdentifier(String externalIdentifier);
	//Long countByExternalIdentifier(String externalIdentifier);
}
