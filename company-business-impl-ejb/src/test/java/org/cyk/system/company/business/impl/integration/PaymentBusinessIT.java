package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.model.payment.CashRegister;

public class PaymentBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private String cashRegisterUnlimitedIdentifier="CASHIER001"/*,cashRegisterLimitedIdentifier="CASHIER002"*/;
    
    @Override
    protected void populate() {
    	super.populate();
    	CashRegister  cashRegister = new CashRegister();
    	companyBusinessTestHelper.set(cashRegister, cashRegisterUnlimitedIdentifier);
    	rootBusinessTestHelper.set(cashRegister.getMovementCollection(), "cr001", "0", "0", null, "Entrée", "Sortie");
    	create(cashRegister);
    	
    	/*
    	cashRegister = new CashRegister();
    	companyBusinessTestHelper.set(cashRegister, cashRegisterLimitedIdentifier);
    	rootTestHelper.set(cashRegister.getMovementCollection(), "cr002", "0", "0", "1000000", "Entrée", "Sortie");
    	create(cashRegister);
    	*/
    }
    
    @Override
    protected void businesses() {
    	companyBusinessTestHelper.createCashRegisterMovement(cashRegisterUnlimitedIdentifier, "100000", "100000");
    	companyBusinessTestHelper.createCashRegisterMovement(cashRegisterUnlimitedIdentifier, "100000", "200000");
    	companyBusinessTestHelper.createCashRegisterMovement(cashRegisterUnlimitedIdentifier, "100000", "300000");
    	companyBusinessTestHelper.createCashRegisterMovement(cashRegisterUnlimitedIdentifier, "-80000", "220000");
    	
    }
    
    /* Exceptions */
    

}
