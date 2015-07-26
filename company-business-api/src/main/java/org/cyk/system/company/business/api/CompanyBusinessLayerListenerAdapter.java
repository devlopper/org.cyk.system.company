package org.cyk.system.company.business.api;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.cdi.AbstractBean;

public class CompanyBusinessLayerListenerAdapter extends AbstractBean implements Serializable, CompanyBusinessLayerListener {

	private static final long serialVersionUID = 8314364003615470095L;

	@Override
	public void handleCompanyToInstall(Company company) {}

	@Override
	public void handlePointOfSaleToInstall(File file) {}

	@Override
	public void handleCompanyLogoToInstall(File file) {
		
	}

}
