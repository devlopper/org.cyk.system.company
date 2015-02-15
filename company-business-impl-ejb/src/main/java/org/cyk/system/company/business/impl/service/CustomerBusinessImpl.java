package org.cyk.system.company.business.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.service.CustomerBusiness;
import org.cyk.system.company.model.service.Customer;
import org.cyk.system.company.persistence.api.service.CustomerDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

public class CustomerBusinessImpl extends AbstractActorBusinessImpl<Customer, CustomerDao> implements CustomerBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CustomerBusinessImpl(CustomerDao dao) {
		super(dao);
	}
	
}
