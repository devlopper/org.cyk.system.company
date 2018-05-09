package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.__test__.TestCase;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.junit.Test;

public class CashRegisterBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override protected void populate() {}
        
    @Test
    public void crudOneCashRegister(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	Company company = inject(CompanyBusiness.class).instanciateOne();
    	company.setCode("MC");
    	testCase.create(company);
    	
    	OwnedCompany ownedCompany = inject(OwnedCompanyBusiness.class).instanciateOne();
    	ownedCompany.setCode("MOC");
    	ownedCompany.setCompany(company);
    	ownedCompany.setDefaulted(Boolean.TRUE);
    	testCase.create(ownedCompany);
    	
    	CashRegister cashRegister = inject(CashRegisterBusiness.class).instanciateOne();
    	cashRegister.setCode("CR001");
    	cashRegister.setName("My Cash 001");
    	testCase.create(cashRegister);
    	
    	testCase.clean();
    }
    
    
}
