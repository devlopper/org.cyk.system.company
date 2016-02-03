package org.cyk.system.company.business.impl.integration;

public class AccountingBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
          
    @Override
    protected void businesses() {
    	assertEquals("Count all sale", 0l, saleDao.countAll());
    	updateAccountingPeriod("0.18", "true","false","false");
    	companyBusinessTestHelper.assertCostComputation(companyBusinessLayer.getAccountingPeriodBusiness().findCurrent(),new String[][]{
    		{"0","0","0"},{"4700","717","3983"}
    	});
    	
    	updateAccountingPeriod("0.18", "true","false","false");
    	companyBusinessTestHelper.assertCostComputation(companyBusinessLayer.getAccountingPeriodBusiness().findCurrent(),new String[][]{
    		{"0","0","0"},{"4700","846","4700"}
    	});
    }
    
    /**/
    
    
    
}
