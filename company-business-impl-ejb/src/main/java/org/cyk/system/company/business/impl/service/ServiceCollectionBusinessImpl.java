package org.cyk.system.company.business.impl.service;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.service.ServiceCollectionBusiness;
import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.company.persistence.api.service.ServiceCollectionDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public class ServiceCollectionBusinessImpl extends AbstractEnumerationBusinessImpl<ServiceCollection, ServiceCollectionDao> implements ServiceCollectionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ServiceCollectionBusinessImpl(ServiceCollectionDao dao) {
		super(dao);
	}

	@Override
	public Collection<ServiceCollection> findAllWithService() {
		return dao.readAllWithService();
	}
	
}
