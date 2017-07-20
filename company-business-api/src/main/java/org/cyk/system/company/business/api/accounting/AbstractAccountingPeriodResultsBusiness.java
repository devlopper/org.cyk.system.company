package org.cyk.system.company.business.api.accounting;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.chart.PieModel;
import org.cyk.system.root.model.AbstractEnumeration;

public interface AbstractAccountingPeriodResultsBusiness<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,PRODUCT extends AbstractEnumeration> extends TypedBusiness<RESULTS> {

	Collection<RESULTS> findByAccountingPeriod(AccountingPeriod accountingPeriod);
	
	RESULTS findByAccountingPeriodByProduct(AccountingPeriod accountingPeriod,PRODUCT product);
	Collection<RESULTS> findByAccountingPeriodByProducts(AccountingPeriod accountingPeriod,Collection<PRODUCT> products);

	Collection<RESULTS> findHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	Collection<RESULTS> findLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products);
	
	BigDecimal findHighestNumberOfSalesValue(Collection<RESULTS> resultsCollection);
	BigDecimal findLowestNumberOfSalesValue(Collection<RESULTS> resultsCollection);
	
	PieModel findNumberOfSalesPieModel(AccountingPeriod accountingPeriod,Collection<PRODUCT> products);
	PieModel findNumberOfSalesPieModel(Collection<RESULTS> results);
	
	PieModel findTurnoverPieModel(Collection<RESULTS> results);
}
