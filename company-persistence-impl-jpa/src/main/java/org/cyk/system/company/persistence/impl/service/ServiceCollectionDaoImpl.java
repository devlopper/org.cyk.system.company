package org.cyk.system.company.persistence.impl.service;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.company.persistence.api.service.ServiceCollectionDao;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

public class ServiceCollectionDaoImpl extends AbstractEnumerationDaoImpl<ServiceCollection> implements ServiceCollectionDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readAllWithService;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readAllWithService, "SELECT DISTINCT sc FROM ServiceCollection sc LEFT JOIN FETCH sc.collection");
	}
	
	@Override
	public Collection<ServiceCollection> readAllWithService() {
		return namedQuery(readAllWithService)
                .resultMany();
	}

}
