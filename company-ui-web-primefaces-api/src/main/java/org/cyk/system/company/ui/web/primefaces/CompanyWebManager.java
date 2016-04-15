package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyWebManager.DEPLOYMENT_ORDER) @Getter
public class CompanyWebManager extends AbstractCompanyWebManager implements Serializable {

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
