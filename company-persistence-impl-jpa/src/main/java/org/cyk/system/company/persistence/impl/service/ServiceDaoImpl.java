package org.cyk.system.company.persistence.impl.service;

import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceType;
import org.cyk.system.company.persistence.api.service.ServiceDao;
import org.cyk.system.root.persistence.impl.pattern.tree.AbstractDataTreeDaoImpl;

public class ServiceDaoImpl extends AbstractDataTreeDaoImpl<Service,ServiceType> implements ServiceDao {

	private static final long serialVersionUID = 6920278182318788380L;

}
