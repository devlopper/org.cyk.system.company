package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.ui.web.primefaces.ServletContextListener;
import org.cyk.utility.common.security.Shiro;

@javax.servlet.annotation.WebListener
public class ContextListener extends ServletContextListener implements Serializable {
	private static final long serialVersionUID = -9042005596731665575L;
	
	/*@Override
	public void __contextInitialized__(ServletContextEvent event) {
		super.__contextInitialized__(event);
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
						//saleCashRegisterMovementCollection.getSale().getCustomer().getPerson().getNames()
						,{"GET IT FROM CUSTOMER",Constant.EMPTY_STRING}	
						,{"Received from",Constant.EMPTY_STRING}
						,{saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().getSenderOrReceiverPersonAsString(),Constant.EMPTY_STRING}	
					});
				}
			}
		});
	}*/
	
	protected void __addFoldersForUser__(Shiro.Ini ini){
		ini.addFoldersForUser("private1");
	}
	
}
