package org.cyk.system.company.business.impl.integration;

import org.junit.Test;

public class StockBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void populate() {
    	super.populate();
    	createProducts(4, 4);
    	createStockableTangibleProducts(new String[][]{ {"TP1", "0","100"},{"TP3", "5","35"},{"TP4", null,null} });
    }
    
    @Override
    protected void businesses() {
    	super.businesses();
    	companyBusinessTestHelper.createStockTangibleProductMovement("TP1", "5");
    	companyBusinessTestHelper.assertStockableTangibleProduct("TP1", "5");
    	companyBusinessTestHelper.createStockTangibleProductMovement("TP1", "5");
    	companyBusinessTestHelper.assertStockableTangibleProduct("TP1", "10");
    	companyBusinessTestHelper.createStockTangibleProductMovement("TP1", "-3");
    	companyBusinessTestHelper.assertStockableTangibleProduct("TP1", "7");
    	
    	companyBusinessTestHelper.createStockTangibleProductMovement("TP4", "-1000000000000");
    	companyBusinessTestHelper.assertStockableTangibleProduct("TP4", "-1000000000000");
    	
    	companyBusinessTestHelper.createStockTangibleProductMovement("TP4", "3000000000000");
    	companyBusinessTestHelper.assertStockableTangibleProduct("TP4", "2000000000000");
    }
    
    /* Exceptions */
    
    /*@Test
    public void incrementValueMustNotBeLessThanIntervalLow(){
    	rootBusinessTestHelper.incrementValueMustNotBeLessThanIntervalLow("TP1_movcol");
    }*/
    @Test
    public void incrementValueMustNotBeGreaterThanIntervalHigh(){
    	rootBusinessTestHelper.incrementValueMustNotBeGreaterThanIntervalHigh("TP1_movcol");
    }
    @Test
    public void decrementValueMustNotBeLessThanIntervalLow(){
    	rootBusinessTestHelper.decrementValueMustNotBeLessThanIntervalLow("TP1_movcol");
    }
    /*@Test
    public void decrementValueMustNotBeGreaterThanIntervalHigh(){
    	rootBusinessTestHelper.decrementValueMustNotBeGreaterThanIntervalHigh("TP1_movcol");
    }*/
    
    @Test
    public void collectionValueMustNotBeLessThanIntervalLow(){
    	rootBusinessTestHelper.collectionValueMustNotBeLessThanIntervalLow("TP1_movcol");
    }
    
    @Test
    public void collectionValueMustNotBeGreaterThanIntervalHigh(){
    	rootBusinessTestHelper.collectionValueMustNotBeGreaterThanIntervalHigh("TP1_movcol");
    }
    
}
