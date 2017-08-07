package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.CustomerBusiness;

public class CustomerBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Override
    protected void businesses() {
    	
    	create(inject(CustomerBusiness.class).instanciateOneRandomly("01"));
    	create(inject(CustomerBusiness.class).instanciateOneRandomly("02"));
    }
    
    /* Exceptions */
    

}
