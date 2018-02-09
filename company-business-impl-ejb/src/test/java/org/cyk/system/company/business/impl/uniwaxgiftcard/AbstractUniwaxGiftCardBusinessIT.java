package org.cyk.system.company.business.impl.uniwaxgiftcard;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;

public abstract class AbstractUniwaxGiftCardBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;
	
    @Inject protected UniwaxGiftCardFakedDataProducer dataProducer;
    @Inject protected FiniteStateMachineStateDao finiteStateMachineStateDao;
    
    @Override
    protected void listeners() {
    	super.listeners();
    	
    	
    	CompanyBusinessLayer.Listener.COLLECTION.add(new CompanyBusinessLayer.Listener.Adapter() {
			private static final long serialVersionUID = 5179809445850168706L;

			@Override
			public String getCompanyName() {
				return "Gestion des cartes cadeaux";
			}
			
			@Override
			public void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod) {
				accountingPeriod.getSaleConfiguration().setAllowOnlySalableProductInstanceOfCashRegister(Boolean.TRUE);
				accountingPeriod.getSaleConfiguration().setMinimalNumberOfProductBySale(1l);
				accountingPeriod.getSaleConfiguration().setMaximalNumberOfProductBySale(1l);
				super.handleAccountingPeriodToInstall(accountingPeriod);
			}
			
		});
    }
    
}
