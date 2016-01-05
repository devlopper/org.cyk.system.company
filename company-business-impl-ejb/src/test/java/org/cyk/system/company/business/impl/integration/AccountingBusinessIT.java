package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

public class AccountingBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
          
    @Override
    protected void businesses() {
    	assertEquals("Count all sale", 0l, saleDao.countAll());
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.TRUE);
    	companyBusinessTestHelper.assertCostComputation(companyBusinessLayer.getAccountingPeriodBusiness().findCurrent(),new String[][]{
    		{"0","0","0"},{"4700","717","3983"}
    	});
    	
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.FALSE);
    	companyBusinessTestHelper.assertCostComputation(companyBusinessLayer.getAccountingPeriodBusiness().findCurrent(),new String[][]{
    		{"0","0","0"},{"4700","846","4700"}
    	});
    }
    
    /**/
    
    
    
}
