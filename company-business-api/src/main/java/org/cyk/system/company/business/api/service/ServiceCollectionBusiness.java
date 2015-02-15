package org.cyk.system.company.business.api.service;

import java.util.Collection;

import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;

public interface ServiceCollectionBusiness extends AbstractEnumerationBusiness<ServiceCollection> {

	Collection<ServiceCollection> findAllWithService();
	
}
