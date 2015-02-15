package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.cdi.AbstractBean;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER) @Getter
public class CompanyWebManager extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 7231721191071228908L;

	private static CompanyWebManager INSTANCE;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
	}
	
	public static CompanyWebManager getInstance() {
		return INSTANCE;
	}
	
}
