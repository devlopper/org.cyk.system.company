package org.cyk.system.company.business.impl;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractBusinessLayerAdapter;
import org.cyk.system.root.model.file.File;

public class CompanyBusinessLayerAdapter extends AbstractBusinessLayerAdapter implements CompanyBusinessLayerListener {

	private static final long serialVersionUID = -3717816726680012239L;

	@Override
	public void handleCompanyToInstall(Company company) {}

	@Override
	public void handleCompanyLogoToInstall(File file) {}

	@Override
	public void handlePointOfSaleToInstall(File file) {}

	@Override
	public void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod) {}

	@Override
	public String getCompanyName() {
		return null;
	}

	@Override
	public byte[] getCompanyLogoBytes() {
		return null;
	}

	@Override
	public byte[] getCompanyPointOfSaleBytes() {
		return null;
	}

}
