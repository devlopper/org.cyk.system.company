package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.junit.Assert;

public class PaymentBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private CashRegister cashRegister1,cashRegisterLimited;
    
    @Override
    protected void populate() {
    	
    }
    
    @Override
    protected void businesses() {
    	installApplication();
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setOwnedCompany(ownedCompanyBusiness.findOneRandomly());
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setOwnedCompany(ownedCompanyBusiness.findOneRandomly());
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	create(cashRegisterLimited);
    	
    	
    	
    	
    	//withdraw(cashRegisterLimited, "100", "0");
    	deposit(cashRegisterLimited, "3000000", "0",Boolean.TRUE);
    	/*
    	deposit(cashRegister1, "100000", "100000");
    	deposit(cashRegister1, "100000", "200000");
    	deposit(cashRegister1, "100000", "300000");
    	
    	withdraw(cashRegister1, "80000", "220000");
    	*/
    }
    
   
    private void deposit(CashRegister cashRegister,String amount,String expectedBalance,Boolean exceptionExpected){
    	final CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
    	cashRegisterMovement.setCashRegister(cashRegister);
    	cashRegisterMovement.setAmount(new BigDecimal(amount));
    	
    	if(Boolean.TRUE.equals(exceptionExpected)){
    		new Try("Deux doit être supérieur à un"){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {companyBusinessLayer.getCashRegisterMovementBusiness().deposit(cashRegisterMovement);}
    		}.execute();
    	}else{
    		companyBusinessLayer.getCashRegisterMovementBusiness().deposit(cashRegisterMovement);
    		assertCashRegister(cashRegister, expectedBalance);
    	}
    }
    private void deposit(CashRegister cashRegister,String amount,String expectedBalance){
    	deposit(cashRegister, amount, expectedBalance,Boolean.FALSE);
    }
    
    private void withdraw(CashRegister cashRegister,String amount,String expectedBalance){
    	CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
    	cashRegisterMovement.setCashRegister(cashRegister);
    	cashRegisterMovement.setAmount(new BigDecimal(amount).negate());
    	companyBusinessLayer.getCashRegisterMovementBusiness().withdraw(cashRegisterMovement);
    	assertCashRegister(cashRegister, expectedBalance);
    }
    
    private void assertCashRegister(CashRegister cashRegister,String expectedBalance){
    	cashRegister = (CashRegister) genericBusiness.use(CashRegister.class).find(cashRegister.getIdentifier());
    	Assert.assertEquals(new BigDecimal(expectedBalance), cashRegister.getBalance());
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
