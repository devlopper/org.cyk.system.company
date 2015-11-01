package org.cyk.system.company.business.impl.aibsbakery;

public class AibsBakeryPopulateWithFakedDataBusinessIT extends AibsBakeryAbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
 
    @Override
    protected void businesses() {
    	installApplication();
    	System.exit(0);
    }
    
}
