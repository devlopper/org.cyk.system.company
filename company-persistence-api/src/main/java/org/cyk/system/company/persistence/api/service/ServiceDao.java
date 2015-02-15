package org.cyk.system.company.persistence.api.service;

import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceType;
import org.cyk.system.root.persistence.api.pattern.tree.AbstractDataTreeDao;

public interface ServiceDao extends AbstractDataTreeDao<Service,ServiceType> {

}
