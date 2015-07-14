package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class SaleStockInputBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private SaleStockInputBusiness saleStockInputBusiness;
    @Inject private SaleBusiness saleBusiness;
    @Inject private CustomerBusiness customerBusiness;
    @Inject private EmployeeBusiness employeeBusiness;
    @Inject private CashierBusiness cashierBusiness;
    @Inject private AccountingPeriodBusiness accountingPeriodBusiness;
    
    @Inject private OwnedCompanyBusiness ownedCompanyBusiness;
     
    //private IntangibleProduct product;
    private CashRegister cashRegister1,cashRegisterLimited;
    private Customer customer1,customer2;
           
    @Override
    protected void businesses() {
    	fakeInstallation();
    	
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent();
    	accountingPeriod.setValueAddedTaxRate(new BigDecimal("0.0318"));
    	accountingPeriod.setValueAddedTaxIncludedInCost(Boolean.FALSE);
    	accountingPeriodBusiness.update(accountingPeriod);
    	
    	customer1 = new Customer();
    	customer1.setPerson(RootRandomDataProvider.getInstance().person(Boolean.TRUE, RootBusinessLayer.getInstance().getCountryCoteDivoire(), RootBusinessLayer.getInstance().getLandPhoneNumberType()));
    	customerBusiness.create(customer1); 
    	
    	customer2 = new Customer();
    	customer2.setPerson(RootRandomDataProvider.getInstance().person(Boolean.TRUE, RootBusinessLayer.getInstance().getCountryCoteDivoire(), RootBusinessLayer.getInstance().getLandPhoneNumberType()));
    	customerBusiness.create(customer2); 
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	cashRegisterLimited.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegisterLimited);
    	
    	Employee employee = employeeBusiness.find().one();
    	Cashier cashier = cashierBusiness.findByEmployee(employee);
    	SaleStockInput saleStockInput = saleStockInputBusiness.newInstance(cashier.getEmployee().getPerson());
    	saleStockInput.getSale().setCompleted(Boolean.FALSE);
    	saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal("3"));
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(saleStockInput.getSale(), new CashRegisterMovement(cashier.getCashRegister()));
    	SaleProduct saleProduct = saleStockInput.getSale().getSaleProducts().iterator().next();
    	saleProduct.setPrice(new BigDecimal("1000"));
    	saleProduct.setQuantity(new BigDecimal("1"));
    	saleBusiness.applyChange(saleStockInput.getSale(), saleProduct);
    	saleStockInput.getSale().setCustomer(customer1);
    	//saleStockInput.getSale().setValueAddedTax(new BigDecimal("0"));
    	saleStockInput.getSale().setCommission(new BigDecimal("0"));
    	
    	saleCashRegisterMovement.getCashRegisterMovement().setAmount(new BigDecimal("0"));
    	saleCashRegisterMovement.setAmountIn(new BigDecimal("0"));
    	
    	saleStockInputBusiness.create(saleStockInput,saleCashRegisterMovement);
    	//Assert.assertEquals("Balance",new BigDecimal("0").doubleValue()+"", saleStockInput.getSale().getBalance().doubleValue()+"");
    	//writeReport(saleBusiness.findReport(Arrays.asList(saleStockInput.getSale())));
    	//debug(saleStockInput.getSale());
    	
    	//saleStockInput.getSale().setValueAddedTax(new BigDecimal("31.8"));
    	saleStockInput.getSale().setAutoComputeValueAddedTax(Boolean.TRUE);
    	saleStockInput.getSale().setCommission(new BigDecimal("200"));
    	saleStockInput.getSale().setCompleted(Boolean.TRUE);
    	saleBusiness.complete(saleStockInput.getSale(),saleCashRegisterMovement);
    	//Assert.assertEquals("Balance",new BigDecimal("1231.8").doubleValue()+"", saleStockInput.getSale().getBalance().doubleValue()+"");
    	
    	//writeReport(saleBusiness.findReport(Arrays.asList(saleStockInput.getSale())));
    	//debug(saleStockInput.getSale());
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
