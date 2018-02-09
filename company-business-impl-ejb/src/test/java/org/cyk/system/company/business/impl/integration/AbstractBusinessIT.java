package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.IntangibleProductDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.AbstractBusinessException;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.business.impl.file.report.AbstractRootReportProducer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineFinalStateDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.system.root.persistence.impl.PersistenceIntegrationTestHelper;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.ThrowableHelper;
import org.cyk.utility.common.test.TestEnvironmentListener;
import org.cyk.utility.test.ArchiveBuilder;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.MatcherAssert.*;

public abstract class AbstractBusinessIT extends AbstractIntegrationTestJpaBased {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Deployment
    public static Archive<?> createDeployment() {
    	Archive<?> archive = createRootDeployment();
    	return archive;
    }
	
	@Inject protected ApplicationBusiness applicationBusiness;
	@Inject protected ExceptionUtils exceptionUtils;
	@Inject protected DefaultValidator defaultValidator;
	@Inject private GenericDaoImpl g;
	@Inject protected GenericBusiness genericBusiness;
	
	@Inject protected ValidatorMap validatorMap;// = ValidatorMap.getInstance();
	@Inject protected RootBusinessLayer rootBusinessLayer;
	@Inject protected RootBusinessTestHelper rootBusinessTestHelper;
	@Inject protected RootDataProducerHelper rootDataProducerHelper;
	@Inject protected CompanyBusinessLayer companyBusinessLayer;
	@Inject protected CompanyBusinessTestHelper companyBusinessTestHelper;
	
	@Inject protected SaleStockTangibleProductMovementBusiness saleStockBusiness;
    @Inject protected SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject protected CustomerBusiness customerBusiness;
    @Inject protected EmployeeBusiness employeeBusiness;
    @Inject protected CashierBusiness cashierBusiness;
    @Inject protected AccountingPeriodBusiness accountingPeriodBusiness;
    @Inject protected OwnedCompanyBusiness ownedCompanyBusiness;
    @Inject protected ProductBusiness productBusiness;
	@Inject protected TangibleProductBusiness tangibleProductBusiness;
	
	@Inject protected ProductDao productDao;
	@Inject protected TangibleProductDao tangibleProductDao;
	@Inject protected IntangibleProductDao intangibleProductDao;
	
	@Inject protected SaleDao saleDao;
	@Inject protected AccountingPeriodProductDao accountingPeriodProductDao;
	@Inject protected SalableProductDao salableProductDao;
	@Inject protected FiniteStateMachineStateDao finiteStateMachineStateDao;
	@Inject protected FiniteStateMachineFinalStateDao finiteStateMachineFinalStateDao;
	@Inject protected UserTransaction userTransaction;
    
	static {
		TestEnvironmentListener.COLLECTION.add(new TestEnvironmentListener.Adapter.Default(){
			private static final long serialVersionUID = 1983969363248568780L;
			@Override
			protected Throwable getThrowable(Throwable throwable) {
				return ThrowableHelper.getInstance().getInstanceOf(throwable, AbstractBusinessException.class);
			}
    		@Override
    		public void assertEquals(String message, Object expected, Object actual) {
    			Assert.assertEquals(message, expected, actual);
    		}
    		@Override
    		public String formatBigDecimal(BigDecimal value) {
    			return inject(NumberBusiness.class).format(value);
    		}
    	});
		ClassHelper.getInstance().map(ApplicationBusinessImpl.Listener.class, ApplicationBusinessAdapter.class,Boolean.FALSE);
	}
	
	protected CompanyBusinessTestHelper.TestCase instanciateTestCase(){
		return companyBusinessTestHelper.instanciateTestCase();
	}
	
    @Override
    public EntityManager getEntityManager() {
        return g.getEntityManager();
    }
	
    @Override
    protected void _execute_() {
        super._execute_();
        create();    
        read(); 
        update();    
        delete();    
        finds();
        businesses();
    }
    
    @Override
    protected void populate() {
    	CompanyBusinessLayer.DATA_SET_CLASS = FakedDataSet.class;
    	installApplication();
    	AbstractRootReportProducer.DEFAULT = inject(CompanyReportProducer.class);
    }
    
    @Override
    protected Boolean populateInTransaction() {
    	return Boolean.FALSE;
    }
    
    protected void finds() {}
    protected void businesses() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
	
	/* Shortcut */
    
    protected void updateAccountingPeriod(String valueAddedTaxRate, String valueAddedTaxIncludedInCost,String balanceCanBeNegative,String balanceCanBeGreaterThanCost){
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent();
    	accountingPeriod.getSaleConfiguration().setValueAddedTaxRate(new BigDecimal(valueAddedTaxRate));
    	accountingPeriod.getSaleConfiguration().setValueAddedTaxIncludedInCost(Boolean.parseBoolean(valueAddedTaxIncludedInCost));
    	accountingPeriod.getSaleConfiguration().setBalanceCanBeNegative(Boolean.parseBoolean(balanceCanBeNegative));
    	accountingPeriod.getSaleConfiguration().setBalanceCanBeGreaterThanCost(Boolean.parseBoolean(balanceCanBeGreaterThanCost));
    	accountingPeriodBusiness.update(accountingPeriod);
    }
    
    protected AbstractIdentifiable create(AbstractIdentifiable object){
        return genericBusiness.create(object);
    }
    @SuppressWarnings("unchecked")
	protected <T extends AbstractIdentifiable> void create(Collection<T> objects){
        genericBusiness.create((Collection<AbstractIdentifiable>) objects);
    }
    
    protected AbstractIdentifiable update(AbstractIdentifiable object){
        return genericBusiness.update(object);
    }
    
    protected void listeners(){
    	
    }
    
    protected void installApplication(){
    	listeners();
    	companyBusinessLayer.installApplication();
    }
        
    public static Archive<?> createRootDeployment() {
        return  
                new ArchiveBuilder().create().getArchive().
                    addClasses(BusinessIntegrationTestHelper.classes()).
                    addClasses(PersistenceIntegrationTestHelper.classes()).
                    addPackages(Boolean.FALSE, BusinessIntegrationTestHelper.packages()).
                    addPackages(Boolean.TRUE,"org.cyk.system.company") 
                    .addClasses(RootBusinessLayer.class,RootBusinessTestHelper.class,CompanyBusinessLayer.class)
                    
                ;
    } 
	
	/**/
	
	public static class ApplicationBusinessAdapter extends ApplicationBusinessImpl.Listener.Adapter.Default implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@Override
		public void installationStarted(Installation installation) {
			super.installationStarted(installation);
			installation.setIsCreateAccounts(Boolean.FALSE);
			installation.setIsCreateLicence(Boolean.FALSE);
			installation.getApplication().setUniformResourceLocatorFiltered(Boolean.FALSE);
			installation.getApplication().setWebContext("company");
			installation.getApplication().setName("CompanyApp");
		}
		
    }
}
