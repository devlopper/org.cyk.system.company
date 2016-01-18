package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootDataProducerHelper;

public abstract class AbstractSaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    public static enum TestMethod{_1_NO_TAX_NOT_PAID}
    
    protected static final String SALE_FINITE_MACHINE_STATE = "custom_sale_finitemachinestate";
    
    protected static final String SALE_FINITE_MACHINE_ALPHABET_VALID = "custom_sale_finitemachinestate_valid";
    
    protected static final String SALE_FINITE_MACHINE_STATE_START = "custom_sale_finitemachinestate_start";
    protected static final String SALE_FINITE_MACHINE_STATE_MIDDLE = "custom_sale_finitemachinestate_middle";
    protected static final String SALE_FINITE_MACHINE_STATE_FINAL = "custom_sale_finitemachinestate_final";
    
    @Override
    protected void populate() {
    	super.populate();
    	RootDataProducerHelper.getInstance().createFiniteStateMachine(SALE_FINITE_MACHINE_STATE
    			, new String[]{SALE_FINITE_MACHINE_ALPHABET_VALID}
    		, new String[]{SALE_FINITE_MACHINE_STATE_START,SALE_FINITE_MACHINE_STATE_MIDDLE,SALE_FINITE_MACHINE_STATE_FINAL}
    		, SALE_FINITE_MACHINE_STATE_START, new String[]{SALE_FINITE_MACHINE_STATE_FINAL}, new String[][]{
    			{SALE_FINITE_MACHINE_STATE_START,SALE_FINITE_MACHINE_ALPHABET_VALID,SALE_FINITE_MACHINE_STATE_MIDDLE}
    			,{SALE_FINITE_MACHINE_STATE_MIDDLE,SALE_FINITE_MACHINE_ALPHABET_VALID,SALE_FINITE_MACHINE_STATE_FINAL}
    	});
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent();
    	accountingPeriod.getSaleConfiguration().setFiniteStateMachine(RootBusinessLayer.getInstance().getFiniteStateMachineBusiness().find(SALE_FINITE_MACHINE_STATE));
    	CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().update(accountingPeriod);
    	
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{"C1","C2","C3","C4","C5","C5np","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"});
    	createProducts(4, 4);
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"TP4", null},{"IP2", "700"} });
    }
           
    @Override
    protected void businesses() {
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.TRUE);
    	
    	companyBusinessTestHelper.createSale("nt1", null, null, "C1", new String[][]{{"TP1","2"}}, "0","false");
    	noTax1NotPaid();
    
    	companyBusinessTestHelper.createSale("nt2", null, null, "C2", new String[][]{{"IP2","3"}}, "2100","false");
    	noTax2AllPaid();	
    
    	companyBusinessTestHelper.createSale("nt3", null, null, "C3", new String[][]{{"TP3","3"}}, "600","false");
    	noTax3SomePaid();
    	
    	companyBusinessTestHelper.createSale("nt4", null, null, "C4", new String[][]{{"TP3","2"}}, "1800","false");
    	noTax4MorePaid1();
    	
    	companyBusinessTestHelper.createSale("nt5", null, null, "C5", new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","false");
    	noTax5MorePaid2();
    	
    	companyBusinessTestHelper.createSale("nt6", null, null, "C5", new String[][]{{"TP3","2"}}, "1800","false");
    	noTax6MorePaid3();
    	
    	companyBusinessTestHelper.createSale("nt7", null, null, "C5np", new String[][]{{"TP4","1","3700"}}, "3700","false");
    	noTax7AllPaidNoUnitPrice();
    	
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
    
    protected abstract void noTax1NotPaid();
    protected abstract void noTax2AllPaid();
    protected abstract void noTax3SomePaid();
    protected abstract void noTax4MorePaid1();
    protected abstract void noTax5MorePaid2();
    protected abstract void noTax6MorePaid3();
    protected abstract void noTax7AllPaidNoUnitPrice();
    protected abstract void noTax8AllPaidUnitPriceButCostValueSet();
                
}
