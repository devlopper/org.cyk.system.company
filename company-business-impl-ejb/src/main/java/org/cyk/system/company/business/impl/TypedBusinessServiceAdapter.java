package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.AbstractIdentifiable;

//TODO should be deleted not necessary
public class TypedBusinessServiceAdapter<IDENTIFIABLE extends AbstractIdentifiable> extends AbstractTypedBusinessService.Listener.Adapter.Default<IDENTIFIABLE> implements Serializable {

	private static final long serialVersionUID = 8466321538111354702L;

	/*protected void createCustomer(IDENTIFIABLE identifiable){
		Customer customer = inject(CustomerBusiness.class).instanciateOne(actor);
		inject(CustomerBusiness.class).create(customer);
	}*/
	
}
