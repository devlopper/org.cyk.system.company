package org.cyk.system.company.persistence.api.accounting;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractAccountingPeriodResultsDao<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,PRODUCT extends AbstractEnumeration> extends TypedDao<RESULTS> {

	RESULTS readByAccountingPeriodByProduct(AccountingPeriod accountingPeriod,PRODUCT product);
	Collection<RESULTS> readByAccountingPeriodByProducts(AccountingPeriod accountingPeriod,Collection<PRODUCT> products);

	Collection<RESULTS> readByAccountingPeriod(AccountingPeriod accountingPeriod);
	
	BigDecimal readHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	BigDecimal readLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	
	Collection<RESULTS> readByAccountingPeriodByProductsByNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products,BigDecimal numberOfSales);
	
}
