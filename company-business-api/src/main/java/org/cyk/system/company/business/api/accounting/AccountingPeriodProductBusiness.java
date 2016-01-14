package org.cyk.system.company.business.api.accounting;

import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Sale;

public interface AccountingPeriodProductBusiness extends AbstractAccountingPeriodResultsBusiness<AccountingPeriodProduct,Product> {

	void consume(Sale sale);

	
}
