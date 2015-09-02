package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.CustomerReportTableRow;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.party.person.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private static final Boolean PRINT = Boolean.FALSE;
    
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
    	
    	sellAndPayAtSameTime(person);
    	//sellAndPayInOne(person);
    	//sellAndPayInOneWithNegativeBalance(person);
    	//sellAndPayInMany(person);
    	
    	computeByCriterias();
    	
    	/*
    	searchByCriterias();
    	
    	printReports();
    	*/
    }
    
    private void sellAndPayAtSameTime(Person person){
    	companyBusinessTestHelper.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"1000",PRINT && Boolean.TRUE, "1000", "153", "0","0");
    }
    
    private void sellAndPayInOne(Person person){
    	Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "1000",PRINT && Boolean.TRUE, "0","0");
    }
    
    private void sellAndPayInOneWithNegativeBalance(Person person){
    	Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "1500",PRINT && Boolean.TRUE, "-500","-500");
    }
    
    private void sellAndPayInMany(Person person){
    	Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "700",PRINT && Boolean.TRUE, "300","300");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "300",PRINT && Boolean.TRUE, "0","0");
    }
    /*
    private void sellAndPayInManyWithNegativeBalance(Person person){
    	Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "700",Boolean.TRUE, "300","300");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "300",Boolean.TRUE, "0","0");
    }*/
    
    private void searchByCriterias(){
    	/*
    	//SaleStock
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,12,31), 18l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, null, null, 18l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,1,31), 13l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", null, null, 2l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,1), date(2015,12,31), 3l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", null, null, 3l);
    	
    	//Sale Stock Input
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,12,31), 6l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, null, null, 6l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,1,31), 4l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,2), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", null, null, 1l);
    	
    	//Sale Stock Output
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,12,31), 12l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, null, null, 12l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,1,31), 9l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,3), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", null, null, 2l);
    	*/
    }
    /*
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
    */
    private void printReports(){
        ReportBasedOnDynamicBuilderParameters<CustomerReportTableRow> customerBalanceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        customerBalanceParameters.setIdentifiableClass(Customer.class);
        customerBalanceParameters.setModelClass(CustomerReportTableRow.class);
        customerBalanceParameters.addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
        		CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
        customerBalanceParameters.addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), 
        		CompanyReportRepository.getInstance().getParameterCustomerBalanceAll());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(customerBalanceParameters);
    }
    
    private void computeByCriterias(){
    	SaleSearchCriteria saleSearchCriteria = new SaleSearchCriteria();
    	companyBusinessTestHelper.saleComputeByCriteria(saleSearchCriteria, "1000", "153", "1000", "0");
    }
    
}
