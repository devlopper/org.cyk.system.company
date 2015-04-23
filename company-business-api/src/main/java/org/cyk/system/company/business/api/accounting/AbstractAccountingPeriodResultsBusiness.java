package org.cyk.system.company.business.api.accounting;

import java.util.Collection;

import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.AbstractIdentifiable;

public interface AbstractAccountingPeriodResultsBusiness<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,PRODUCT extends AbstractIdentifiable> extends TypedBusiness<RESULTS> {

	Collection<RESULTS> findByAccountingPeriod(AccountingPeriod accountingPeriod);
	
	RESULTS findByAccountingPeriodByProduct(AccountingPeriod accountingPeriod,PRODUCT product);

	Collection<RESULTS> findHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	Collection<RESULTS> findLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	
}
