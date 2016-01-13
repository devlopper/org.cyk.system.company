package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.sale.AbstractSaleStockBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.CustomerReportTableRow;
import org.cyk.system.company.business.impl.sale.SaleStockReportTableRow;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SaleStock;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockSearchCriteria;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.party.person.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class SaleStockBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private static final Boolean PRINT_REPORT = Boolean.TRUE;
    
    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
     
    private CashRegister cashRegister1;
    private Customer customer1,customer2,customer3;
    private Person person;
    
    private void init(){
    	installApplication();
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent();
    	//accountingPeriod.setValueAddedTaxRate(new BigDecimal("0.18"));
    	//accountingPeriod.setValueAddedTaxIncludedInCost(Boolean.TRUE);
    	accountingPeriod.getStockConfiguration().setZeroQuantityAllowed(Boolean.TRUE);
    	accountingPeriodBusiness.update(accountingPeriod);
    	
    	customer1 = new Customer();
    	customer1.setPerson(RootRandomDataProvider.getInstance().person());
    	customerBusiness.create(customer1); 
    	
    	customer2 = new Customer();
    	customer2.setPerson(RootRandomDataProvider.getInstance().person());
    	customerBusiness.create(customer2); 
    	
    	customer3 = new Customer();
    	customer3.setPerson(RootRandomDataProvider.getInstance().person());
    	customerBusiness.create(customer3); 
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegister1);
    	
    	//person = companyBusinessTestHelper.cashierPerson();
    }
    
    @Override
    protected void businesses() {
    	init();
    	//Person person = companyBusinessTestHelper.cashierPerson();
    	
    	dropAndTakeInOne0(person);
    	//SaleStockInputSearchCriteria criteria = new SaleStockInputSearchCriteria();
    	//companyBusinessTestHelper.saleStockInputComputeByCriteria(criteria, "3","0","1000", "0", "1000", "0");
    	
    	//dropAndTakeInOne(person);
    	/*
    	dropAndTakeInMany(person);
    	dropAndTakeInManyWithZeroPayment(person);
    	dropAndTakeInManyWithZeroQuantity(person);
    	dropManyAndTakeInMany(person);
    	dropAndTakeInManyWithPaymentAtZeroQuantity(person);
    	*/
    	//searchByCriterias();
    	
    	//printReports();
    	
    	
    }
    
    private void dropAndTakeInOne0(Person person){
    	Boolean print = PRINT_REPORT && Boolean.TRUE;
    	Boolean value = companyBusinessTestHelper.getSaleAutoCompleted();
    	companyBusinessTestHelper.setSaleAutoCompleted(Boolean.FALSE);
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 1),person, customer1,"A", "1000", "0", "3",print, "1000", "0", "0","0");
    	companyBusinessTestHelper.setSaleAutoCompleted(value);
    	
    	companyBusinessTestHelper.complete(date(2015, 1, 1),person, saleStockInput, "0",print, "1000", "153", "1000","1000");
    	
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1000",print, "0", "0","0");
    	
    	
    }
    
    private void dropAndTakeInOne(Person person){
    	Boolean print = PRINT_REPORT && Boolean.TRUE;
    	SaleStockInput saleStockInput = companyBusinessTestHelper
        		.drop(date(2015, 1, 1),person, customer1,"A", "1000", "100", "3",print, "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1100",print, "0", "0","0");
    	
    	saleStockInput = companyBusinessTestHelper
        		.drop(date(2015, 1, 1),person, customer2,"A1", "1000", "100", "3",print, "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1100",print, "0", "0","0");
    }
    
    private void dropAndTakeInMany(Person person){
    	Boolean print = PRINT_REPORT && Boolean.TRUE;
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 3),person, customer1,"B", "1000", "100", "3",print, "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 4),person, saleStockInput, "2", "800",print, "1", "300","300");
    	companyBusinessTestHelper.taking(date(2015, 1, 5),person, saleStockInput, "1", "300",print, "0", "0","0");
    	
    	saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 3),person, customer2,"B1", "1000", "100", "3",print, "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 4),person, saleStockInput, "2", "800",print, "1", "300","300");
    	companyBusinessTestHelper.taking(date(2015, 1, 5),person, saleStockInput, "1", "300",print, "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroQuantity(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 6),person, customer1,"C", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 7),person, saleStockInput, "0", "800", "3", "300","300");
    	companyBusinessTestHelper.taking(date(2015, 1, 8),person, saleStockInput, "2", "100", "1", "200","200");
    	companyBusinessTestHelper.taking(date(2015, 1, 9),person, saleStockInput, "1", "200", "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroPayment(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 10),person, customer1,"D", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 11),person, saleStockInput, "1", "0", "2", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 12),person, saleStockInput, "1", "1000", "1", "100","100");
    	companyBusinessTestHelper.taking(date(2015, 1, 13),person, saleStockInput, "1", "100", "0", "0","0");
    }
    
    private  void dropAndTakeInManyWithPaymentAtZeroQuantity(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 10),person, customer3,"DK", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 11),person, saleStockInput, "3", "600", "0", "500","500");
    	companyBusinessTestHelper.taking(date(2015, 1, 12),person, saleStockInput, "0", "500", "0", "0","0");
    }
    
    private void dropManyAndTakeInMany(Person person){
    	SaleStockInput saleStockInput1 = companyBusinessTestHelper.drop(date(2015, 2, 1),person, customer1,"E", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 2, 2),person, saleStockInput1, "1", "400", "2", "700","700");
    	
    	SaleStockInput saleStockInput2 = companyBusinessTestHelper.drop(date(2015, 2, 3),person, customer1,"F", "1500", "100", "5", "1600", "245", "1600","2300");
    	companyBusinessTestHelper.taking(date(2015, 2, 4),person, saleStockInput2, "2", "1000", "3", "600","1300");
    	
    	companyBusinessTestHelper.taking(date(2015, 2, 5),person, saleStockInput1, "1", "300", "1", "400","1000");
    }
    
    private void searchByCriterias(){
    	//SaleStock
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,12,31), 23l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, null, null, 23l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,1,31), 18l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", null, null, 2l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,1), date(2015,12,31), 3l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", null, null, 3l);
    	
    	//Sale Stock Input
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,12,31), 8l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, null, null, 8l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,1,31), 6l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,2), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", null, null, 1l);
    	
    	//Sale Stock Output
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,12,31), 15l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, null, null, 15l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,1,31), 12l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,3), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", null, null, 2l);
    	
    }
    
    private <ENTITY extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> void searchByCriteria(Class<ENTITY> saleStockClass,
    		Class<SEARCH_CRITERIA> searchCriteriaClass,AbstractSaleStockBusiness<ENTITY, SEARCH_CRITERIA> business,String externalIdentifier,Date fromDate,Date toDate,Long expectedCount){
    	SEARCH_CRITERIA searchCriteria = null;
		try {
			searchCriteria = searchCriteriaClass.newInstance();
			if(searchCriteria instanceof SaleStockOutputSearchCriteria)
				((SaleStockOutputSearchCriteria)searchCriteria).setMinimumPaid(BigDecimal.ZERO);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	searchCriteria.getFromDateSearchCriteria().setValue(fromDate); 
    	searchCriteria.getToDateSearchCriteria().setValue(toDate);
    	searchCriteria.getExternalIdentifierStringSearchCriteria().setValue(externalIdentifier);
    	Assert.assertEquals("Count by search criteria", expectedCount, business.countByCriteria(searchCriteria));
    	if(StringUtils.isNotBlank(externalIdentifier))
	    	for(ENTITY saleStock : business.findByCriteria(searchCriteria)){
	    		SaleStockInput saleStockInput = saleStock instanceof SaleStockInput?((SaleStockInput)saleStock):
	    			((SaleStockOutput)saleStock).getSaleStockInput();
	    		Assert.assertEquals("External identifier",externalIdentifier, saleStockInput.getExternalIdentifier());
	    	}
    		
    }
    
    private void printReports(){
    	/*
    	ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCashRegisterParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCashRegisterParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCashRegisterParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCashRegisterParameters.addParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(), CompanyReportRepository.getInstance().getParameterSaleStockReportCashRegister());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCashRegisterParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputInventoryParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputInventoryParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputInventoryParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputInventoryParameters.addParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(), CompanyReportRepository.getInstance().getParameterSaleStockReportInventory());
        rootTestHelper.addReportParameterFromDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputInventoryParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCustomerParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCustomerParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCustomerParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCustomerParameters.addParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(), 
        		CompanyReportRepository.getInstance().getParameterSaleStockReportCustomer());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCustomerParameters);
        
        ReportBasedOnDynamicBuilderParameters<CustomerReportTableRow> customerBalanceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        customerBalanceParameters.setIdentifiableClass(Customer.class);
        customerBalanceParameters.setModelClass(CustomerReportTableRow.class);
        customerBalanceParameters.addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
        		CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
        customerBalanceParameters.addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), 
        		CompanyReportRepository.getInstance().getParameterCustomerBalanceAll());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(customerBalanceParameters);
        */
    }
    
    /* Exceptions */
    /*
    @Test(expected=BusinessException.class)
    public void dropAndTakeTooManyGoodsThanInStock(){
    	person = companyBusinessTestHelper.cashierPerson();
    	SaleStockInput saleStockInput = companyBusinessTestHelper
        		.drop(date(2015, 1, 1),person, customer1,"ZZZ", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1100", "0", "0","0");
    }
    */
    
}
