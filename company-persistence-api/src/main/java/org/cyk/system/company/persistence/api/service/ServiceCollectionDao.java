package org.cyk.system.company.persistence.api.service;

import java.util.Collection;

import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.root.persistence.api.AbstractEnumerationDao;

public interface ServiceCollectionDao extends AbstractEnumerationDao<ServiceCollection> {

	Collection<ServiceCollection> readAllWithService();
   
}
