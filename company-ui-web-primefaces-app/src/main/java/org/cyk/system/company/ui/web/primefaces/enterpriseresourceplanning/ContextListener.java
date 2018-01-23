package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.AbstractCompanyReportProducer;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollectionReportTemplateFile;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.AbstractContextListener;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.PrimefacesManager;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.root.model.party.Application;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.web.primefaces.resources.ComponentAdapter;
import org.cyk.ui.web.primefaces.resources.PrimefacesResourcesManager;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.security.Shiro;
import org.cyk.utility.common.userinterface.Component;

@javax.servlet.annotation.WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		CompanyWebManager.getInstance().getListeners().add(new PrimefacesManager());
		CompanyBusinessLayer.getInstance().enableEnterpriseResourcePlanning();
		
		AbstractCompanyReportProducer.Listener.COLLECTION.add(new AbstractCompanyReportProducer.Listener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void processLabelValueCollection(AbstractIdentifiableReport<?> identifiable,LabelValueCollectionReport labelValueCollection) {
				super.processLabelValueCollection(identifiable,labelValueCollection);
				if(SaleCashRegisterMovementCollectionReportTemplateFile.LABEL_VALUES_PAYMENT.equals(labelValueCollection.getIdentifier())){
					SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = (SaleCashRegisterMovementCollection) identifiable.getSource();
					labelValueCollection.getCollection().clear();
					labelValueCollection.addLabelValues(new String[][]{
						{"Date",Constant.EMPTY_STRING}
						,{identifiable.getCreationDate(),Constant.EMPTY_STRING}
						,{"Receipt No.",Constant.EMPTY_STRING}
						,{identifiable.getCode(),Constant.EMPTY_STRING}
						,{"Cashier Name",Constant.EMPTY_STRING}
						,{ saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner() instanceof Application ? saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner().getName() 
								: ((Person)saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner()).getNames(),Constant.EMPTY_STRING}
						,{"Parent",Constant.EMPTY_STRING}
						,{"GET IT FROM CUSTOMER"/*saleCashRegisterMovementCollection.getSale().getCustomer().getPerson().getNames()*/,Constant.EMPTY_STRING}	
						,{"Received from",Constant.EMPTY_STRING}
						,{saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().getSenderOrReceiverPersonAsString(),Constant.EMPTY_STRING}	
					});
				}
			}
			
		});
		
		/* TO BE FACTORED */
		inject(PrimefacesResourcesManager.class).initializeContext(event);
		ClassHelper.getInstance().map(Component.Listener.class,ComponentAdapter.class);
		
		Shiro.Ini ini = Shiro.Ini.getInstance().clean();
		ini.addUsers("admin", "123","user1","123","user2","123");
		ini.addFoldersForUser("private");
		ini.addLoginUrl("/public/security/login.jsf");
	}
	
}
