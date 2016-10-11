package org.cyk.system.company.business.impl.adapter;

import java.io.Serializable;

import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.party.person.AbstractActor;

public class ActorBusinessServiceAdapter<ACTOR extends AbstractActor> extends AbstractTypedBusinessService.Listener.Adapter.Default<ACTOR> implements Serializable {

	private static final long serialVersionUID = -8384047237353201461L;

	//public static final Set<Class<?>> ARE_CUSTOMERS = new HashSet<>();
	/*
	@Override
	public void processOnCreated(ACTOR actor) {
		super.processOnCreated(actor);
		if(Boolean.TRUE.equals(isCustomer(actor))){
			Customer customer = inject(CustomerBusiness.class).instanciateOne(actor);
			inject(CustomerBusiness.class).create(customer);
		}
	}
	*/
	/**/
	/*
	protected Boolean isCustomer(ACTOR actor){
		return ARE_CUSTOMERS.contains(actor.getClass());
	}
	*/
}
