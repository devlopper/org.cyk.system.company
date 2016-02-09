package org.cyk.system.company.business.impl.integration.sale;

import org.junit.Test;

public class SaleExceptionsBusinessIT extends AbstractSaleBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Test
    public void balanceMustBeGreaterThanZero(){
    	companyBusinessTestHelper.balanceMustBeGreaterThanZero();
    }
    
    @Test
    public void balanceCannotBeIncrementedBeforeSoldOut(){
    	companyBusinessTestHelper.balanceCannotBeIncrementedBeforeSoldOut();
    }
    
    @Test
    public void balanceMustBeLowerThanCost(){
    	companyBusinessTestHelper.balanceMustBeLowerThanCost();
    }
    
}
