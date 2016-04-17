package org.cyk.system.company.business.impl.adapter;

import java.io.Serializable;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.party.person.AbstractActor;

public class ActorBusinessServiceAdapter<ACTOR extends AbstractActor> extends AbstractTypedBusinessService.Listener.Adapter.Default<ACTOR> implements Serializable {

	private static final long serialVersionUID = -8384047237353201461L;

	@Override
	public void processOnCreated(ACTOR actor) {
		super.processOnCreated(actor);
		if(Boolean.TRUE.equals(isCustomer())){
			Customer customer = CompanyBusinessLayer.getInstance().getCustomerBusiness().instanciateOne(actor);
			CompanyBusinessLayer.getInstance().getCustomerBusiness().create(customer);
		}
	}
	
	/**/
	
	protected Boolean isCustomer(){
		return Boolean.FALSE;
	}
	
}
