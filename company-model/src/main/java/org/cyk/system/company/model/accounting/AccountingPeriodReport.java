package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.sale.SaleConfigurationReport;
import org.cyk.system.company.model.structure.CompanyReport;
import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor
public class AccountingPeriodReport extends AbstractGeneratable<AccountingPeriod> implements Serializable {

	private static final long serialVersionUID = 7967195187341455422L;
	
	private CompanyReport company = new CompanyReport();
	private SaleConfigurationReport saleConfiguration = new SaleConfigurationReport();
	
	public AccountingPeriodReport(AccountingPeriod accountingPeriod) {
		setSource(accountingPeriod);
	}

	@Override
	public void generate() {
		saleConfiguration.generate();
		company.generate();
	}
	
}
