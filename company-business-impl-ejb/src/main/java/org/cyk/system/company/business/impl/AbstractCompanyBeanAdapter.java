package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.utility.common.cdi.BeanAdapter;

public abstract class AbstractCompanyBeanAdapter extends BeanAdapter implements Serializable {

	private static final long serialVersionUID = 4013856635471619938L;

	protected CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
}
