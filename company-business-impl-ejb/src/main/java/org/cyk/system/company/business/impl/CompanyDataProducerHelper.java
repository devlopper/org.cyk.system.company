package org.cyk.system.company.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.utility.common.cdi.AbstractBean;

@Singleton
public class CompanyDataProducerHelper extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 8420754068573843133L;

	protected CompanyBusinessLayer getCompanyBusinessLayer(){
		return CompanyBusinessLayer.getInstance();
	}
	
	
	
}
