package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyEditPage;
import org.cyk.ui.api.config.IdentifiableConfiguration;
import org.cyk.ui.web.primefaces.AbstractContextListener;

public abstract class AbstractCompanyContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -6496963238759008827L;

	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerConfiguration(new IdentifiableConfiguration(Company.class, CompanyEditPage.Form.class, CompanyDetails.class,null));
		uiManager.configBusinessIdentifiable(Company.class, null);
		
	}
	
}
