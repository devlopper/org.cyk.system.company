package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class PaymentBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
        return createRootDeployment();
    } 
    
    @Inject private CashRegisterMovementBusiness cashRegisterMovementBusiness;
    //@Inject private CashRegisterBusiness cashRegisterBusiness;
    
    private CashRegister cashRegister1,cashRegisterLimited;
    
    @Override
    protected void populate() {
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	create(cashRegisterLimited);
    }
    
    @Override
    protected void businesses() {
    	try{
    		//withdraw(cashRegisterLimited, "100", "0");
    		//deposit(cashRegisterLimited, "3000000", "0");
    	}catch(Exception exception){
    		
    	}
    	deposit(cashRegister1, "100000", "100000");
    	deposit(cashRegister1, "100000", "200000");
    	deposit(cashRegister1, "100000", "300000");
    	
    	withdraw(cashRegister1, "80000", "220000");
    }
    
   
    private void deposit(CashRegister cashRegister,String amount,String balance){
    	//cashRegisterMovementBusiness.deposit(new CashRegisterMovement(null, cashRegister, new BigDecimal(amount), null));
    	//assertBalance(cashRegister, balance);
    }
    
    private void withdraw(CashRegister cashRegister,String amount,String balance){
    	//cashRegisterMovementBusiness.withdraw(new CashRegisterMovement(null, cashRegister, new BigDecimal(amount), null));
    	//assertBalance(cashRegister, balance);
    }
    
    private void assertBalance(CashRegister cashRegister,String balance){
    	CashRegister cr = (CashRegister) genericBusiness.use(CashRegister.class).find(cashRegister.getIdentifier());
    	Assert.assertEquals(new BigDecimal(balance), cr.getBalance());
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
