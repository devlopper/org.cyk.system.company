package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;

//@WebListener
public class ContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
	}
	
	
	
}
