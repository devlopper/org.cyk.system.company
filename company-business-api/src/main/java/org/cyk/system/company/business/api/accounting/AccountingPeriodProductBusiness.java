package org.cyk.system.company.business.api.accounting;

import java.util.Collection;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SaleProduct;

public interface AccountingPeriodProductBusiness extends AbstractAccountingPeriodResultsBusiness<AccountingPeriodProduct,Product> {

	void consume(AccountingPeriod accountingPeriod,Collection<SaleProduct> saleProducts);

	
}
