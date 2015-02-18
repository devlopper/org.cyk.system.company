package org.cyk.system.company.persistence.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.root.persistence.impl.party.person.AbstractActorDaoImpl;

public class CustomerDaoImpl extends AbstractActorDaoImpl<Customer> implements CustomerDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

}
