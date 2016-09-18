package org.cyk.system.company.ui.web.primefaces.iesa;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.AbstractContextListener;

@javax.servlet.annotation.WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@SuppressWarnings("unchecked")
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		CompanyWebManager.getInstance().getListeners().add(new PrimefacesManager());
		
		EmployeeBusinessImpl.Listener employeeListener = new EmployeeBusinessImpl.Listener.Adapter.Default();
		employeeListener.addCascadeToClasses(EmploymentAgreement.class);
		EmployeeBusinessImpl.Listener.COLLECTION.add(employeeListener);
	}
	
}
