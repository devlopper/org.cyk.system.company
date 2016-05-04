package org.cyk.system.company.business.api.accounting;

import java.math.BigDecimal;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.event.AbstractIdentifiablePeriodBusiness;

public interface AccountingPeriodBusiness extends AbstractIdentifiablePeriodBusiness<AccountingPeriod> {

	void close(AccountingPeriod accountingPeriod);
	
	AccountingPeriod findCurrent(OwnedCompany company);
	AccountingPeriod findCurrent();
	
	AccountingPeriod findPrevious(OwnedCompany company);
	AccountingPeriod findPrevious();
	/*
	void process(SaleProduct saleProduct);
	*/
	BigDecimal computeValueAddedTax(AccountingPeriod accountingPeriod,BigDecimal amount);
	BigDecimal computeTurnover(AccountingPeriod accountingPeriod,BigDecimal amount,BigDecimal valueAddedTax);
	/*
	BigDecimal computeTurnover(BigDecimal cost,BigDecimal valueAddedTax);
	BigDecimal computeTurnover(Sale sale);
	*/
	
}
