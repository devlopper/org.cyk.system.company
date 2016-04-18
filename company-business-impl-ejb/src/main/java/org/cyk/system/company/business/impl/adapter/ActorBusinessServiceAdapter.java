package org.cyk.system.company.business.impl.adapter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.party.person.AbstractActor;

public class ActorBusinessServiceAdapter<ACTOR extends AbstractActor> extends AbstractTypedBusinessService.Listener.Adapter.Default<ACTOR> implements Serializable {

	private static final long serialVersionUID = -8384047237353201461L;

	public static final Set<Class<?>> ARE_CUSTOMERS = new HashSet<>();
	
	@Override
	public void processOnCreated(ACTOR actor) {
		super.processOnCreated(actor);
		if(Boolean.TRUE.equals(isCustomer(actor))){
			Customer customer = CompanyBusinessLayer.getInstance().getCustomerBusiness().instanciateOne(actor);
			CompanyBusinessLayer.getInstance().getCustomerBusiness().create(customer);
		}
	}
	
	/**/
	
	protected Boolean isCustomer(ACTOR actor){
		return ARE_CUSTOMERS.contains(actor.getClass());
	}
	
}