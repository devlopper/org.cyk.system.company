package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.party.person.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    private CashRegister cashRegister1;
    private Customer customer1,customer2;
    
    private void init(){
    	installApplication();
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent();
    	accountingPeriod.setValueAddedTaxRate(new BigDecimal("0.18"));
    	accountingPeriod.setValueAddedTaxIncludedInCost(Boolean.TRUE);
    	accountingPeriod.getStockConfiguration().setZeroQuantityAllowed(Boolean.TRUE);
    	accountingPeriodBusiness.update(accountingPeriod);
    	
    	customer1 = new Customer();
    	customer1.setPerson(RootRandomDataProvider.getInstance().person());
    	customerBusiness.create(customer1); 
    	
    	customer2 = new Customer();
    	customer2.setPerson(RootRandomDataProvider.getInstance().person());
    	customerBusiness.create(customer2); 
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegister1);
    	
    	TangibleProduct tp = new TangibleProduct("tp1", "P1", null, null, new BigDecimal("1000"));
    	tp.setUseQuantity(new BigDecimal("1000"));
    	create(tp);
    	
    }
        
    @Override
    protected void businesses() {
    	init();
    	Person person = companyBusinessTestHelper.cashierPerson();
    	companyBusinessTestHelper.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"1000",Boolean.TRUE, null, null, null,null);
    	companyBusinessTestHelper.setSaleAutoCompleted(Boolean.FALSE);
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 1),person, customer1,"A", "1000", "0", "3",Boolean.TRUE, null,null,null,null);
    	companyBusinessTestHelper.complete(date(2015, 1, 1),person, saleStockInput, "0",Boolean.TRUE,null,null,null,null);
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1000",Boolean.TRUE, null,null,null);
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
}
