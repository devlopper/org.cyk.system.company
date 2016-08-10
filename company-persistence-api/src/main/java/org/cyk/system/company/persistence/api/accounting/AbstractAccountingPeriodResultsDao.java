package org.cyk.system.company.persistence.api.accounting;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.persistence.api.TypedDao;

public interface AbstractAccountingPeriodResultsDao<RESULTS extends AbstractAccountingPeriodResults<ENTITY>,ENTITY extends AbstractEnumeration> extends TypedDao<RESULTS> {

	RESULTS readByAccountingPeriodByEntity(AccountingPeriod accountingPeriod,ENTITY product);
	Collection<RESULTS> readByAccountingPeriodByEntities(AccountingPeriod accountingPeriod,Collection<ENTITY> products);

	Collection<RESULTS> readByAccountingPeriod(AccountingPeriod accountingPeriod);
	Collection<RESULTS> readByEntity(ENTITY product);
	
	BigDecimal readHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<ENTITY> products);
	BigDecimal readLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<ENTITY> products);
	
	Collection<RESULTS> readByAccountingPeriodByEntitiesByNumberOfSales(AccountingPeriod accountingPeriod, Collection<ENTITY> products,BigDecimal numberOfSales);
	
}
