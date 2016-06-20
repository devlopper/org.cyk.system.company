package org.cyk.system.company.business.impl.uniwaxgiftcard;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.CompanyBusinessLayerAdapter;
import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.security.Installation;

public abstract class AbstractUniwaxGiftCardBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

	private static final String GIFT_CARD_WORKFLOW = "GIFT_CARD_WORKFLOW";
	private static final String GIFT_CARD_ASSIGNED = "GIFT_CARD_ASSIGNED";
	private static final String GIFT_CARD_SEND = "GIFT_CARD_SEND";
	private static final String GIFT_CARD_SENT = "GIFT_CARD_SENT";
	private static final String GIFT_CARD_RECEIVE = "GIFT_CARD_RECEIVE";
	private static final String GIFT_CARD_RECEIVED = "GIFT_CARD_RECEIVED";
	private static final String GIFT_CARD_SELL = "GIFT_CARD_SELL";
	private static final String GIFT_CARD_SOLD = "GIFT_CARD_SOLD";
	private static final String GIFT_CARD_USE = "GIFT_CARD_USE";
	private static final String GIFT_CARD_USED = "GIFT_CARD_USED";
	
    @Inject protected UniwaxGiftCardFakedDataProducer dataProducer;
     
    protected void installApplication(Boolean fake){
    	super.installApplication(fake);
    }
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
    
    @Override
    protected void listeners() {
    	super.listeners();
    	ApplicationBusinessImpl.Listener.COLLECTION.add(new ApplicationBusinessImpl.Listener.Adapter.Default(){
			private static final long serialVersionUID = 6148913289155659043L;
			@Override
    		public void installationStarted(Installation installation) {
    			installation.getApplication().setUniformResourceLocatorFilteringEnabled(Boolean.FALSE);
    			installation.getApplication().setWebContext("company");
    			installation.getApplication().setName("Uniwax - Gestion des cartes cadeaux");
    			super.installationStarted(installation);
    		}
    	});
    	
    	companyBusinessLayer.getCompanyBusinessLayerListeners().add(new CompanyBusinessLayerAdapter() {
			private static final long serialVersionUID = 5179809445850168706L;

			@Override
			public String getCompanyName() {
				return "Gestion des cartes cadeaux";
			}
			
			@Override
			public void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod) {
				FiniteStateMachine finiteStateMachine = rootDataProducerHelper.createFiniteStateMachine(GIFT_CARD_WORKFLOW
		    			, new String[]{GIFT_CARD_SEND,GIFT_CARD_RECEIVE,GIFT_CARD_SELL,GIFT_CARD_USE}
						, new String[]{GIFT_CARD_ASSIGNED,GIFT_CARD_SENT,GIFT_CARD_RECEIVED,GIFT_CARD_SOLD,GIFT_CARD_USED}
		    			, GIFT_CARD_ASSIGNED, new String[]{GIFT_CARD_USED}, new String[][]{
		    			{GIFT_CARD_ASSIGNED,GIFT_CARD_SEND,GIFT_CARD_SENT}
		    			,{GIFT_CARD_SENT,GIFT_CARD_RECEIVE,GIFT_CARD_RECEIVED}
		    			,{GIFT_CARD_RECEIVED,GIFT_CARD_SELL,GIFT_CARD_SOLD}
		    			,{GIFT_CARD_SOLD,GIFT_CARD_USE,GIFT_CARD_USED}
		    	});
				accountingPeriod.getSaleConfiguration().setSalableProductInstanceCashRegisterFiniteStateMachine(finiteStateMachine);
				accountingPeriod.getSaleConfiguration().setAllowOnlySalableProductInstanceOfCashRegister(Boolean.TRUE);
				accountingPeriod.getSaleConfiguration().setMinimalNumberOfProductBySale(1l);
				accountingPeriod.getSaleConfiguration().setMaximalNumberOfProductBySale(1l);
				super.handleAccountingPeriodToInstall(accountingPeriod);
			}
			
			/*@Override
			public void handleCompanyToInstall(Company company) {
				super.handleCompanyToInstall(company);
				addContacts(company.getContactCollection(), new String[]{"RueJ7 1-II Plateux Vallon, Cocody"}, new String[]{"22417217","21014459"}
				, new String[]{"05996283","49925138","06173731"}, new String[]{"08 BP 1828 Abidjan 08"}, new String[]{"iesa@aviso.ci"}, new String[]{"http://www.iesaci.com"});
			}*/

		});
    }
    
}
