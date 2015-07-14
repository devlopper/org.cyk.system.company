package org.cyk.system.company.business.impl.integration;

import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyRandomDataProvider;
import org.cyk.system.company.business.impl.product.CredenceReportTableDetails;
import org.cyk.system.company.business.impl.product.CustomerBalanceReportTableDetails;
import org.cyk.system.company.business.impl.product.SaleReportTableDetail;
import org.cyk.system.company.business.impl.product.TangibleProductInventoryReportTableDetails;
import org.cyk.system.company.business.impl.product.TangibleProductStockMovementLineReport;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private RandomDataProvider randomDataProvider;
    @Inject private RootRandomDataProvider rootRandomDataProvider;
    @Inject private CompanyRandomDataProvider companyRandomDataProvider;
    @Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
    
    @Override
    protected void businesses() {
    	fakeInstallation();
    	rootRandomDataProvider = RootRandomDataProvider.getInstance();
    	rootRandomDataProvider.createActor(Customer.class, 100);
    	companyRandomDataProvider.createSale(100);
        //companyRandomDataProvider.createTangibleProductStockMovement(100);
        //companyRandomDataProvider.createTangibleProductInventory(100);
        
        /*
        Collection<Class<?>> classes = new ArrayList<>();
        classes.add(EventType.class);
        classes.add(TimeDivisionType.class);
        for(Class<?> clazz : classes){
        	rootTestHelper.reportBasedOnDynamicBuilderParameters(clazz);
        }*/
        /*
        ReportBasedOnDynamicBuilderParameters<TangibleProductStockMovementLineReport> tpsmlrParameters = new ReportBasedOnDynamicBuilderParameters<>();
        tpsmlrParameters.setIdentifiableClass(TangibleProductStockMovement.class);
        tpsmlrParameters.setModelClass(TangibleProductStockMovementLineReport.class);
        rootTestHelper.addReportParameterFromDate(tpsmlrParameters, DateUtils.addDays(new Date(), -randomDataProvider.randomInt(1, 10)));
        rootTestHelper.addReportParameterToDate(tpsmlrParameters, DateUtils.addDays(new Date(), randomDataProvider.randomInt(1, 10)));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(tpsmlrParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleReportTableDetail> sParameters = new ReportBasedOnDynamicBuilderParameters<>();
        sParameters.setIdentifiableClass(Sale.class);
        sParameters.setModelClass(SaleReportTableDetail.class);
        rootTestHelper.addReportParameterFromDate(sParameters, DateUtils.addDays(new Date(), -randomDataProvider.randomInt(1, 10)));
        rootTestHelper.addReportParameterToDate(sParameters, DateUtils.addDays(new Date(), randomDataProvider.randomInt(1, 10)));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(sParameters);
        
        for(TangibleProductInventory tangibleProductInventory : tangibleProductInventoryBusiness.findAll()){
	        ReportBasedOnDynamicBuilderParameters<TangibleProductInventoryReportTableDetails> tpiParameters = new ReportBasedOnDynamicBuilderParameters<>();
	        tpiParameters.setIdentifiableClass(TangibleProductInventory.class);
	        tpiParameters.setModelClass(TangibleProductInventoryReportTableDetails.class);
	        BusinessEntityInfos tangibleProductInventoryEntityInfos = applicationBusiness.findBusinessEntityInfos(TangibleProductInventory.class);
	        tpiParameters.addParameter(tangibleProductInventoryEntityInfos.getIdentifier(), tangibleProductInventory.getIdentifier());
	        rootTestHelper.reportBasedOnDynamicBuilderParameters(tpiParameters);
        }
        */
        
        ReportBasedOnDynamicBuilderParameters<CustomerBalanceReportTableDetails> customerBalanceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        customerBalanceParameters.setIdentifiableClass(Customer.class);
        customerBalanceParameters.setModelClass(CustomerBalanceReportTableDetails.class);
        customerBalanceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerBalanceAll());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(customerBalanceParameters);
        
        ReportBasedOnDynamicBuilderParameters<CustomerBalanceReportTableDetails> credenceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        credenceParameters.setIdentifiableClass(Customer.class);
        credenceParameters.setModelClass(CustomerBalanceReportTableDetails.class);
        credenceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerBalanceCredence());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(credenceParameters);
        
        
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
}
