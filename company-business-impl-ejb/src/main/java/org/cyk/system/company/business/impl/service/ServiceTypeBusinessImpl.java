package org.cyk.system.company.business.impl.service;

import javax.inject.Inject;

import org.cyk.system.company.business.api.service.ServiceTypeBusiness;
import org.cyk.system.company.model.service.ServiceType;
import org.cyk.system.company.persistence.api.service.ServiceTypeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ServiceTypeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ServiceType,ServiceTypeDao> implements ServiceTypeBusiness {

	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public ServiceTypeBusinessImpl(ServiceTypeDao dao) {
        super(dao);
    } 

}
