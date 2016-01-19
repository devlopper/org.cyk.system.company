package org.cyk.system.company.business.impl.integration.sale;

import java.math.BigDecimal;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootDataProducerHelper;

public abstract class AbstractSaleWithManyFiniteStateMachineStateBusinessIT extends AbstractSaleBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
      
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
    }
           
    @Override
    protected void businesses() {
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.TRUE);
    	
    	CreateSaleParameters p = new CreateSaleParameters("nt1", null, null, "C1", new String[][]{{"TP1","2"}}, null,"false");
    	createSale(p);
    	noTax1_1CreateSaleInitialState(p);
    	
    	UpdateSaleParameters u = new UpdateSaleParameters(p.getComputedIdentifier(), SALE_FINITE_MACHINE_ALPHABET_VALID, p.getTaxable());
    	updateSale(u);
    	noTax1_2UpdateSaleMiddleState(u);
    	
    	updateSale(u);
    	noTax1_3UpdateSaleFinalState(u);
    }
    
    protected abstract void noTax1_1CreateSaleInitialState(CreateSaleParameters parameters);
    protected abstract void noTax1_2UpdateSaleMiddleState(UpdateSaleParameters parameters);
    protected abstract void noTax1_3UpdateSaleFinalState(UpdateSaleParameters parameters);
    /*
    protected abstract void noTax2AllPaid(CreateSaleParameters parameters);
    protected abstract void noTax3SomePaid(CreateSaleParameters parameters);
    protected abstract void noTax4MorePaid1(CreateSaleParameters parameters);
    protected abstract void noTax5MorePaid2(CreateSaleParameters parameters);
    protected abstract void noTax6MorePaid3(CreateSaleParameters parameters);
    protected abstract void noTax7AllPaidNoUnitPrice(CreateSaleParameters parameters);
    protected abstract void noTax8AllPaidUnitPriceButCostValueSet(CreateSaleParameters parameters);
                */
}
