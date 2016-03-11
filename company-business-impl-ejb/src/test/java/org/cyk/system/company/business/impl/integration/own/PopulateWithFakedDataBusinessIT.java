package org.cyk.system.company.business.impl.integration.own;


public class PopulateWithFakedDataBusinessIT extends AbstractOwnBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
 
    @Override
    protected void populate() {
    	dataProducer.setDoBusiness(Boolean.TRUE);
    	super.populate();
    }
    
    @Override
    protected void businesses() {
    	System.exit(0);
    }
    
}