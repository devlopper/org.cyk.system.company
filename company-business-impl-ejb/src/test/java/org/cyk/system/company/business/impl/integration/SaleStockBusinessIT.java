package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.party.person.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class SaleStockBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
     
    //private IntangibleProduct product;
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
    }
    
    @Override
    protected void businesses() {
    	init();
    	Employee employee = employeeBusiness.find().one();
    	Cashier cashier = cashierBusiness.findByEmployee(employee);
    	Person person = cashier.getEmployee().getPerson();
    	
    	dropAndTakeInOne(person);
    	dropAndTakeInMany(person);
    	dropAndTakeInManyWithZeroPayment(person);
    	dropAndTakeInManyWithZeroQuantity(person);
    	dropManyAndTakeInMany(person);
    	printReports();
    }
    
    private void dropAndTakeInOne(Person person){
    	SaleStockInput saleStockInput = drop(date(2015, 1, 1),person, customer1, "1000", "100", "3", "1100", "168", "1100","1100");
    	taking(date(2015, 1, 2),person, saleStockInput, "3", "1100", "0", "0","0");
    }
    
    private void dropAndTakeInMany(Person person){
    	SaleStockInput saleStockInput = drop(date(2015, 1, 3),person, customer1, "1000", "100", "3", "1100", "168", "1100","1100");
    	taking(date(2015, 1, 4),person, saleStockInput, "2", "800", "1", "300","300");
    	taking(date(2015, 1, 5),person, saleStockInput, "1", "300", "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroQuantity(Person person){
    	SaleStockInput saleStockInput = drop(date(2015, 1, 6),person, customer1, "1000", "100", "3", "1100", "168", "1100","1100");
    	taking(date(2015, 1, 7),person, saleStockInput, "0", "800", "3", "300","300");
    	taking(date(2015, 1, 8),person, saleStockInput, "2", "100", "1", "200","200");
    	taking(date(2015, 1, 9),person, saleStockInput, "1", "200", "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroPayment(Person person){
    	SaleStockInput saleStockInput = drop(date(2015, 1, 10),person, customer1, "1000", "100", "3", "1100", "168", "1100","1100");
    	taking(date(2015, 1, 11),person, saleStockInput, "1", "0", "2", "1100","1100");
    	taking(date(2015, 1, 12),person, saleStockInput, "1", "1000", "1", "100","100");
    	taking(date(2015, 1, 13),person, saleStockInput, "1", "100", "0", "0","0");
    }
    
    private void dropManyAndTakeInMany(Person person){
    	SaleStockInput saleStockInput1 = drop(date(2015, 2, 1),person, customer1, "1000", "100", "3", "1100", "168", "1100","1100");
    	taking(date(2015, 2, 2),person, saleStockInput1, "1", "400", "2", "700","700");
    	
    	SaleStockInput saleStockInput2 = drop(date(2015, 2, 3),person, customer1, "1500", "100", "5", "1600", "245", "1600","2300");
    	taking(date(2015, 2, 4),person, saleStockInput2, "2", "1000", "3", "600","1300");
    	
    	taking(date(2015, 2, 5),person, saleStockInput1, "1", "300", "1", "400","1000");
    }
    
    private void printReports(){
    	ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCashRegisterParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCashRegisterParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCashRegisterParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCashRegisterParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportType(), CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportCashRegister());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCashRegisterParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputInventoryParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputInventoryParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputInventoryParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputInventoryParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportType(), CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportInventory());
        rootTestHelper.addReportParameterFromDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputInventoryParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCustomerParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCustomerParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCustomerParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCustomerParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportType(), 
        		CompanyBusinessLayer.getInstance().getParameterSaleStockOutputReportCustomer());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCustomerParameters);
    }
    
}
