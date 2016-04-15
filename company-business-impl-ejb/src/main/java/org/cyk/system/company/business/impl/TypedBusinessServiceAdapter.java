package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.AbstractActor;

public class TypedBusinessServiceAdapter<IDENTIFIABLE extends AbstractIdentifiable> extends AbstractTypedBusinessService.Listener.Adapter.Default<IDENTIFIABLE> implements Serializable {

	private static final long serialVersionUID = 8466321538111354702L;

	protected void createCustomer(AbstractActor actor){
		Customer customer = CompanyBusinessLayer.getInstance().getCustomerBusiness().instanciateOne(actor);
		CompanyBusinessLayer.getInstance().getCustomerBusiness().create(customer);
	}
	
}
