package org.cyk.system.company.business.impl.service;

import javax.inject.Inject;

import org.cyk.system.company.business.api.service.ServiceBusiness;
import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceType;
import org.cyk.system.company.persistence.api.service.ServiceDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeBusinessImpl;

public class ServiceBusinessImpl extends AbstractDataTreeBusinessImpl<Service,ServiceDao,ServiceType> implements ServiceBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ServiceBusinessImpl(ServiceDao dao) {
        super(dao);
    }
 
}
