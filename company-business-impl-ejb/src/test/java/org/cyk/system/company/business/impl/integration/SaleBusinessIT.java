package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.CustomerReportTableRow;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.party.person.Person;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    private static final Boolean PRINT = Boolean.TRUE;
         
    @Override
    protected void populate() {
    	super.populate();
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"});
    	createProducts(3, 4);
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"IP2", "700"} });
    }
           
    @Override
    protected void businesses() {
    	assertEquals("Count all sale", 0l, saleDao.countAll());
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.TRUE);
    	//No tax applied
    	//nothing paid
    	companyBusinessTestHelper.createSale("nt1", null, null, "C1", new String[][]{{"TP1","2"}}, "0","false", "2000", "0", "2000", "2000", "2000");
    	//all paid
    	companyBusinessTestHelper.createSale("nt2", null, null, "C2", new String[][]{{"IP2","3"}}, "2100","false", "2100", "0", "2100", "0", "0");
    	//less paid
    	companyBusinessTestHelper.createSale("nt3", null, null, "C3", new String[][]{{"TP3","3"}}, "600","false", "1500", "0", "1500", "900", "900");
    	//more paid
    	companyBusinessTestHelper.createSale("nt4", null, null, "C4", new String[][]{{"TP3","2"}}, "1800","false", "1000", "0", "1000", "-800", "-800");
    	//many items
    	companyBusinessTestHelper.createSale("nt5", null, null, "C5"
    			, new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","false", "4700", "0", "4700", "-300", "-300");
    	companyBusinessTestHelper.createSale("nt6", null, null, "C5", new String[][]{{"TP3","2"}}, "1800","false", "1000", "0", "1000", "-800", "-1100");
    	
    	//Tax applied in cost
    	//nothing paid
    	companyBusinessTestHelper.createSale("t1", null, null, "C6", new String[][]{{"TP1","2"}}, "0","true", "2000", "306", "1694", "2000", "2000");
    	//all paid
    	companyBusinessTestHelper.createSale("t2", null, null, "C7", new String[][]{{"IP2","3"}}, "2100","true", "2100", "321", "1779", "0", "0");
    	//less paid
    	companyBusinessTestHelper.createSale("t3", null, null, "C8", new String[][]{{"TP3","3"}}, "600","true", "1500", "229", "1271", "900", "900");
    	//more paid
    	companyBusinessTestHelper.createSale("t4", null, null, "C9", new String[][]{{"TP3","2"}}, "1800","true", "1000", "153", "847", "-800", "-800");
    	//many items
    	companyBusinessTestHelper.createSale("t5", null, null, "C10"
    			, new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","true", "4700", "717", "3983", "-300", "-300");
    	companyBusinessTestHelper.createSale("t6", null, null, "C10", new String[][]{{"TP3","2"}}, "1800","true", "1000", "153", "847", "-800", "-1100");
    	
    	
    	/*
    	//Tax applied out of cost
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.FALSE);
    	//nothing paid
    	companyBusinessTestHelper.createSale("ot1", null, null, "C11", new String[][]{{"TP1","2"}}, "0","true", "2360", "360", "2000", "2360", "2360");
    	//all paid
    	companyBusinessTestHelper.createSale("ot2", null, null, "C12", new String[][]{{"IP2","3"}}, "2478","true", "2478", "378", "2478", "0", "0");
    	//less paid
    	companyBusinessTestHelper.createSale("ot3", null, null, "C13", new String[][]{{"TP3","3"}}, "600","true", "1500", "270", "1500", "900", "900");
    	//more paid
    	companyBusinessTestHelper.createSale("ot4", null, null, "C14", new String[][]{{"TP3","2"}}, "1800","true", "1000", "180", "1000", "-800", "-800");
    	//many items
    	companyBusinessTestHelper.createSale("ot5", null, null, "C15"
    			, new String[][]{{"TP3","2"},{"IP2","1"},{"TP1","3"}}, "5000","true", "4700", "846", "4700", "-300", "-300");
    	companyBusinessTestHelper.createSale("ot6", null, null, "C15", new String[][]{{"TP3","2"}}, "1800","true", "1000", "180", "1000", "-800", "-1100");
    	*/
    	
    	//companyBusinessTestHelper.createSale("4", null, null, "C1", new String[][]{{"TP1","2"}}, "800", "2000", "0", "2000", "1200", "1200");
    	
    	//assertCost(saleDao.readByComputedIdentifier("1").getCost(),"1000","0","1000");
    	
    	//Person person = companyBusinessTestHelper.cashierPerson();
    	
    	//sellAndPayAtSameTime(person);
    	//sellAndPayInOne(person);
    	//sellAndPayInOneWithNegativeBalance(person);
    	//sellAndPayInMany(person);
    	
    	//computeByCriterias();
    	
    	/*
    	searchByCriterias();
    	
    	printReports();
    	*/
    }
    
    /**/
    
    //@Test
    public void processSaleProduct(){
    	Sale sale = new Sale();
    	//companyBusinessTestHelper.set(sale, null, null, null, null, null);
    	
    	SalableProduct salableProduct = salableProductDao.readByProduct(productDao.read("TP1"));
    	SaleProduct saleProduct = new SaleProduct();
    	saleProduct.setSalableProduct(salableProduct);
    	saleProduct.setQuantity(BigDecimal.ONE);
    	saleProduct.setSale(sale);
    	
    	CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
    	//assertCost(sale.getCost(),"1000","0","0");
    	
    }
    
    /* Exceptions */
    
    
    
    private void sellAndPayAtSameTime(Person person){
    	//companyBusinessTestHelper.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"1000",PRINT && Boolean.TRUE, "1000", "153", "0","0");
    }
    
    private void sellAndPayInOne(Person person){
    	/*Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "1000",PRINT && Boolean.TRUE, "0","0");*/
    }
    
    private void sellAndPayInOneWithNegativeBalance(Person person){
    	/*Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "1500",PRINT && Boolean.TRUE, "-500","-500");*/
    }
    
    private void sellAndPayInMany(Person person){
    	/*Sale sale = companyBusinessTestHelper
        		.sell(date(2015, 1, 1),person, customer1,new String[]{"tp1","1"},"0",PRINT && Boolean.TRUE, "1000", "153", "1000","1000");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "700",PRINT && Boolean.TRUE, "300","300");
    	companyBusinessTestHelper.pay(date(2015, 1, 2),person, sale, "300",PRINT && Boolean.TRUE, "0","0");*/
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
        //rootTestHelper.reportBasedOnDynamicBuilderParameters(customerBalanceParameters);
    }
    
    private void computeByCriterias(){
    	SaleSearchCriteria saleSearchCriteria = new SaleSearchCriteria();
    	companyBusinessTestHelper.saleComputeByCriteria(saleSearchCriteria, "1000", "153", "1000", "0");
    }
    
}
