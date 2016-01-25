package org.cyk.system.company.business.impl.integration.own;


public class PopulateWithFakedDataBusinessIT extends AbstractOwnBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
 
    @Override
    protected void businesses() {
    	dataProducer.setDoBusiness(Boolean.TRUE);
    	installApplication();
    	System.exit(0);
    }
    
}
