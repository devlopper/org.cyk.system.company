package org.cyk.system.company.business.api;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.file.File;

public interface CompanyBusinessLayerListener {
	
	void handleCompanyToInstall(Company company);
	void handleCompanyLogoToInstall(File file);
	void handlePointOfSaleToInstall(File file);
	
}
