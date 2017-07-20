package org.cyk.system.company.business.impl.integration;

public class ProductBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void populate() {
    	super.populate();
    	createProducts(5, 6);
    }
        
    @Override
    protected void businesses() {
    	
    }
    
}
