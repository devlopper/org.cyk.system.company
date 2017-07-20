package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;

public abstract class AbstractCompanyPrimefacesManager extends AbstractPrimefacesManager.AbstractPrimefacesManagerListener.Adapter implements Serializable {

	private static final long serialVersionUID = -8716834916609095637L;
	
	public AbstractCompanyPrimefacesManager() {
		configurePaymentModule();
		configureProductModule();
	}
	
	protected void configurePaymentModule() {}
	
	protected void configureProductModule() {}
	
}
