package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.model.sale.Customer;

public class CustomerBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Override
    protected void businesses() {
    	Customer customer = inject(CustomerBusiness.class).instanciateOneRandomly("01");
    	//customer.getPerson().setContactCollection(null);
    	create(customer);
    	//create(inject(CustomerBusiness.class).instanciateOneRandomly("02"));
    }
    
    /* Exceptions */
    

}
