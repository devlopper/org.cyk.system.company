package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.junit.Test;

public class PaymentBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private String cashRegisterUnlimitedIdentifier="CASHIER001",cashRegisterLimitedIdentifier="CASHIER002";
    
    @Override
    protected void populate() {
    	super.populate();
    	CashRegister  cashRegister = new CashRegister(cashRegisterUnlimitedIdentifier,ownedCompanyBusiness.findOneRandomly(),BigDecimal.ZERO,null,null);
    	create(cashRegister);
    	
    	cashRegister = new CashRegister(cashRegisterLimitedIdentifier,ownedCompanyBusiness.findOneRandomly(),BigDecimal.ZERO,new BigDecimal("0"),new BigDecimal("1000000"));
    	create(cashRegister);
    	
    }
    
    @Override
    protected void businesses() {
    	companyBusinessTestHelper.deposit(cashRegisterUnlimitedIdentifier, "100000", "100000");
    	companyBusinessTestHelper.deposit(cashRegisterUnlimitedIdentifier, "100000", "200000");
    	companyBusinessTestHelper.deposit(cashRegisterUnlimitedIdentifier, "100000", "300000");
    	companyBusinessTestHelper.withdraw(cashRegisterUnlimitedIdentifier, "80000", "220000");
    }
    
    /* Exceptions */
    
    @Test(expected=Exception.class)
    public void balanceGreaterThanMaximumBalance(){
    	companyBusinessTestHelper.depositBalanceGreaterThanMaximumBalance(cashRegisterLimitedIdentifier, "3000000");
    }
    
    @Test(expected=Exception.class)
    public void balanceLowerThanMaximumBalance(){
    	companyBusinessTestHelper.depositBalanceLowerThanMinimumBalance(cashRegisterLimitedIdentifier, "100");
    }
    
   
    

}
