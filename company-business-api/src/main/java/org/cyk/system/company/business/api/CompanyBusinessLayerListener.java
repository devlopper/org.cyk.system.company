package org.cyk.system.company.business.api;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.file.File;

public interface CompanyBusinessLayerListener {
	
	void handleCompanyToInstall(Company company);
	void handleCompanyLogoToInstall(File file);
	void handlePointOfSaleToInstall(File file);
	void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod);
	
	String getCompanyName();
	
	/* File */
	
	byte[] getCompanyLogoBytes();
	byte[] getCompanyPointOfSaleBytes();
	
	/**/

	String SALE_IDENTIFICATION_NUMBER = "SALE_IDENTIFICATION_NUMBER";
	String CASH_MOVEMENT_IDENTIFICATION_NUMBER = "CASH_MOVEMENT_IDENTIFICATION_NUMBER";
	
}
