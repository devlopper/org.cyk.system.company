package org.cyk.system.company.ui.web.primefaces.iesa;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.AbstractContextListener;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.information.Comment;

//@javax.servlet.annotation.WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		Comment.define(Employee.class);
		FileIdentifiableGlobalIdentifier.define(Employee.class);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		CompanyWebManager.getInstance().getListeners().add(new PrimefacesManager());
		
		CompanyBusinessLayer.getInstance().enableEnterpriseResourcePlanning();
		
	}
	
}
