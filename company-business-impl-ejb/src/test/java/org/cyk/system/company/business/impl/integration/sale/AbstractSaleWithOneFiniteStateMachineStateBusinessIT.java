package org.cyk.system.company.business.impl.integration.sale;


public abstract class AbstractSaleWithOneFiniteStateMachineStateBusinessIT extends AbstractSaleBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    protected static final String S1 = "sale_1",S2 = "sale_2",S3 = "sale_3",S4 = "sale_4",S5 = "sale_5";
    protected static final String S1_P1 = "sale_1_pay_1",S1_P2 = "sale_1_pay_2";
    
    @Override
    protected void businesses() {
    	updateAccountingPeriod("0.18", "true","false","false");
    	
    	CreateSaleParameters p = new CreateSaleParameters(S1, null, null, CUST1, new String[][]{{TP1,"2"}}, "0","false");
    	createSale(p);
    	_1(p);
    	/*
    	CreateSaleCashRegisterMovementParameters cashRegisterMovementParameters = new CreateSaleCashRegisterMovementParameters(S1, S1_P1, null, "500");
    	createSaleCashRegisterMovement(cashRegisterMovementParameters);
    	_2(cashRegisterMovementParameters);	
    
    	cashRegisterMovementParameters = new CreateSaleCashRegisterMovementParameters(S1, S1_P2, null, "1000");
    	createSaleCashRegisterMovement(cashRegisterMovementParameters);
    	_3(cashRegisterMovementParameters);
    	*/
    	/*
    	p = new CreateSaleParameters("nt4", null, null, "C4", new String[][]{{"TP3","2"}}, "1800","false");
    	createSale(p);
    	noTax4MorePaid1(p);
    	
    	p = new CreateSaleParameters("nt5", null, null, "C5", new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","false");
    	createSale(p);
    	noTax5MorePaid2(p);
    	
    	p = new CreateSaleParameters("nt6", null, null, "C5", new String[][]{{"TP3","2"}}, "1800","false");
    	createSale(p);
    	noTax6MorePaid3(p);
    	
    	p = new CreateSaleParameters("nt7", null, null, "C5np", new String[][]{{"TP4","1","3700"}}, "3700","false");
    	createSale(p);
    	noTax7AllPaidNoUnitPrice(p);
    	*/

    	//TODO to be handled
    	//companyBusinessTestHelper.createSale("nt8", null, null, "C5np", new String[][]{{"TP1","3","2500"}}, "2500","false", "3700", "0", "3700", "0", "0");
    	//noTax8AllPaidUnitPriceButCostValueSet();
    	
    	/*
    	
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("3", "5600", "0", "5600");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodProductSaleResults("TP3", "3", "1500", "0", "1500");
    	
    	
    	companyBusinessTestHelper.assertCurrentAccountingPeriodProductSaleResults("TP3", "5", "2500", "0", "2500");
    	
    	
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodProductSaleResults("TP1", "2", "2000", "0", "2000");
    	
    	
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodProductSaleResults("TP1", "2", "2000", "0", "2000");
    	
    	//
    	
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("6", "12300", "0", "12300");
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodProductSaleResults("TP1", "2", "2000", "0", "2000");
    	
    	//Tax applied in cost
    	companyBusinessTestHelper.createSale("t1", null, null, "C6", new String[][]{{"TP1","2"}}, "0","true", "2000", "306", "1694", "2000", "2000");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("7", "14300", "306", "13994");
    	
    	companyBusinessTestHelper.createSale("t2", null, null, "C7", new String[][]{{"IP2","3"}}, "2100","true", "2100", "321", "1779", "0", "0");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("8", "16400", "627", "15773");
    	
    	companyBusinessTestHelper.createSale("t3", null, null, "C8", new String[][]{{"TP3","3"}}, "600","true", "1500", "229", "1271", "900", "900");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("9", "17900", "856", "17044");
    	
    	companyBusinessTestHelper.createSale("t4", null, null, "C9", new String[][]{{"TP3","2"}}, "1800","true", "1000", "153", "847", "-800", "-800");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("10", "18900", "1009", "17891");
    	
    	companyBusinessTestHelper.createSale("t5", null, null, "C10"
    			, new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","true", "4700", "717", "3983", "-300", "-300");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("11", "23600", "1726", "21874");
    	
    	companyBusinessTestHelper.createSale("t6", null, null, "C10", new String[][]{{"TP3","2"}}, "1800","true", "1000", "153", "847", "-800", "-1100");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("12", "24600", "1879", "22721");
    	
    	//Tax applied out of cost
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.FALSE);
    	companyBusinessTestHelper.createSale("ot1", null, null, "C11", new String[][]{{"TP1","2"}}, "0","true", "2000", "360", "2000", "2360", "2360");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("13", "26600", "2239", "24721");
    	
    	companyBusinessTestHelper.createSale("ot2", null, null, "C12", new String[][]{{"IP2","3"}}, "2478","true", "2100", "378", "2100", "0", "0");
    	companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("14", "28700", "2617", "26821");
    	
    	companyBusinessTestHelper.createSale("ot3", null, null, "C13", new String[][]{{"TP3","3"}}, "600","true", "1500", "270", "1500", "1170", "1170");
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("15", "26600", "2239", "24721");
    	
    	companyBusinessTestHelper.createSale("ot4", null, null, "C14", new String[][]{{"TP3","2"}}, "1800","true", "1000", "180", "1000", "-620", "-620");
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("16", "26600", "2239", "24721");
    	
    	companyBusinessTestHelper.createSale("ot5", null, null, "C15"
    			, new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","true", "4700", "846", "4700", "546", "546");
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("17", "26600", "2239", "24721");
    	
    	companyBusinessTestHelper.createSale("ot6", null, null, "C15", new String[][]{{"TP3","2"}}, "1800","true", "1000", "180", "1000", "-620", "-74");
    	//companyBusinessTestHelper.assertCurrentAccountingPeriodSaleResults("18", "26600", "2239", "24721");
    	*/
    }
    
    /**
     * No tax , no payment
     * @param parameters
     */
    protected abstract void _1(CreateSaleParameters parameters);
    protected abstract void _2(CreateSaleCashRegisterMovementParameters parameters);
    protected abstract void _3(CreateSaleCashRegisterMovementParameters parameters);
    protected abstract void noTax4MorePaid1(CreateSaleParameters parameters);
    protected abstract void noTax5MorePaid2(CreateSaleParameters parameters);
    protected abstract void noTax6MorePaid3(CreateSaleParameters parameters);
    protected abstract void noTax7AllPaidNoUnitPrice(CreateSaleParameters parameters);
    protected abstract void noTax8AllPaidUnitPriceButCostValueSet(CreateSaleParameters parameters);
    
    /**/
    
    @Override
    protected void createSale(CreateSaleParameters p) {
    	p.setWriteReport(Boolean.FALSE);
    	super.createSale(p);
    }
    
    @Override
    protected void createSaleCashRegisterMovement(CreateSaleCashRegisterMovementParameters p) {
    	p.setWriteReport(Boolean.FALSE);
    	super.createSaleCashRegisterMovement(p);
    }
}
