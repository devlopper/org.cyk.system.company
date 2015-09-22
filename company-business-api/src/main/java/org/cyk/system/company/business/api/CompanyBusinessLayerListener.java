package org.cyk.system.company.business.api;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.generator.StringGenerator;

public interface CompanyBusinessLayerListener {
	
	void handleCompanyToInstall(Company company);
	void handleCompanyLogoToInstall(File file);
	void handlePointOfSaleToInstall(File file);
	void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod);
	
	String getCompanyName();
	
	/* File */
	
	byte[] getCompanyLogoBytes();
	byte[] getCompanyPointOfSaleBytes();
	
	/* Identifier */

	StringGenerator getSaleIdentifierGenerator();
	StringGenerator getCashRegisterMovementIdentifierGenerator();
	
	/**/
	/*
	Collection<Employee> getEmployees();
	Employee getManager();
	
	UserAccount getUserAccount(Person person);
	*/
	/**/
	
	String SALE_IDENTIFIER = "SALE_IDENTIFIER";
	String CASH_MOVEMENT_IDENTIFIER = "CASH_MOVEMENT_IDENTIFIER";
	
}
