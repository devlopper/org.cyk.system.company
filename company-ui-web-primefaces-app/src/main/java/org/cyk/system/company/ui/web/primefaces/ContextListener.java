package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.web.primefaces.AbstractContextListener;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationFormModel;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationPage;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationPage.ApplicationInstallListener;
import org.cyk.ui.web.primefaces.page.tools.ActorCrudOnePageListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	//@Inject private CompanyRandomDataProvider companyRandomDataProvider;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		primefacesManager.getBusinessEntityFormOnePageListeners().add(new ActorCrudOnePageListener<Customer>(Customer.class));
		ApplicationInstallationPage.LISTENERS.add(new ApplicationInstallListener() {
			
			@Override
			public void install(ApplicationInstallationFormModel formModel) {
				/*
				rootRandomDataProvider.createActor(Customer.class, 25);
				rootRandomDataProvider.createActor(Employee.class, 25);
		    	companyRandomDataProvider.createSale(100);
		        companyRandomDataProvider.createTangibleProductStockMovement(25);
		        companyRandomDataProvider.createTangibleProductInventory(25);
		        */
			}
		});
	}
		
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
	}
	
	
	
}
