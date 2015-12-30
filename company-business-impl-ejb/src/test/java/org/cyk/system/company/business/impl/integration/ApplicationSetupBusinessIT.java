package org.cyk.system.company.business.impl.integration;


public class ApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	installApplication();
    	System.exit(0);
    }

    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
