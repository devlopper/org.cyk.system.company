package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;


public class PopulateWithFakedDataBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

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
