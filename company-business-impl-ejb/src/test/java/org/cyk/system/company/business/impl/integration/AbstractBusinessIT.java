package org.cyk.system.company.business.impl.integration;

import java.io.FileOutputStream;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.MatcherAssert.*;
import org.apache.commons.io.IOUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.AbstractBusinessException;
import org.cyk.system.root.business.api.BusinessLayer;
import org.cyk.system.root.business.api.BusinessLayerListener;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer.FakedDataProducerAdapter;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.AbstractReport;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.system.root.persistence.impl.PersistenceIntegrationTestHelper;
import org.cyk.utility.common.test.TestEnvironmentListener;
import org.cyk.utility.test.ArchiveBuilder;
import org.cyk.utility.test.Transaction;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

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
	@Inject protected ProductDao productDao;

	@Inject protected ValidatorMap validatorMap;// = ValidatorMap.getInstance();
	@Inject protected RootBusinessLayer rootBusinessLayer;
	@Inject protected RootBusinessTestHelper rootBusinessTestHelper;
	@Inject protected RootDataProducerHelper rootDataProducerHelper;
	@Inject protected CompanyBusinessLayer companyBusinessLayer;
	@Inject protected CompanyBusinessTestHelper companyBusinessTestHelper;
	
	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
    @Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
    @Inject protected SaleStockBusiness saleStockBusiness;
    @Inject protected SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject protected CustomerBusiness customerBusiness;
    @Inject protected EmployeeBusiness employeeBusiness;
    @Inject protected CashierBusiness cashierBusiness;
    @Inject protected AccountingPeriodBusiness accountingPeriodBusiness;
    @Inject protected OwnedCompanyBusiness ownedCompanyBusiness;
    @Inject protected ProductBusiness productBusiness;
	@Inject protected TangibleProductBusiness tangibleProductBusiness;
	
	@Inject protected CashRegisterDao cashRegisterDao;
	@Inject protected UserTransaction userTransaction;
    
	static {
		TestEnvironmentListener.COLLECTION.add(new TestEnvironmentListener.Adapter.Default(){
			private static final long serialVersionUID = 1983969363248568780L;
			@Override
			protected Throwable getThrowable(Throwable throwable) {
				return commonUtils.getThrowableInstanceOf(throwable, AbstractBusinessException.class);
			}
    		@Override
    		public void assertEquals(String message, Object expected, Object actual) {
    			Assert.assertEquals(message, expected, actual);
    		}
    		@Override
    		public String formatBigDecimal(BigDecimal value) {
    			return RootBusinessLayer.getInstance().getNumberBusiness().format(value);
    		}
    	});
	}
	
	protected AbstractFakedDataProducer getFakedDataProducer(){
    	return null;
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
    	installApplication();
    }
    
    @Override
    protected Boolean populateInTransaction() {
    	return Boolean.TRUE;
    }
    
    protected void finds() {}
    protected void businesses() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
	
	/* Shortcut */
    
    protected AbstractIdentifiable create(AbstractIdentifiable object){
        return genericBusiness.create(object);
    }
    
    protected AbstractIdentifiable update(AbstractIdentifiable object){
        return genericBusiness.update(object);
    }
    
    protected void installApplication(Boolean fake){
    	companyBusinessLayer.installApplication(fake);
    }
    
    protected void installApplication(){
    	RootBusinessLayer.getInstance().getBusinessLayerListeners().add(new BusinessLayerListener.Adapter.Default(){
			private static final long serialVersionUID = 6148913289155659043L;
			@Override
    		public void beforeInstall(BusinessLayer businessLayer,Installation installation) {
    			installation.getApplication().setUniformResourceLocatorFilteringEnabled(Boolean.FALSE);
    			installation.getApplication().setWebContext("company");
    			installation.getApplication().setName("CompanyApp");
    			super.beforeInstall(businessLayer, installation);
    		}
    	});
    	installApplication(Boolean.TRUE);
    	produce(getFakedDataProducer());
    }
    
    protected void produce(final AbstractFakedDataProducer fakedDataProducer){
    	if(fakedDataProducer==null)
    		return ;
    	new Transaction(this,userTransaction,null){
			@Override
			public void _execute_() {
				fakedDataProducer.produce(fakedDataProducerAdapter());
			}
    	}.run();
    }
    
    protected FakedDataProducerAdapter fakedDataProducerAdapter(){
    	return new FakedDataProducerAdapter(){
    		@Override
    		public void flush() {
    			super.flush();
    			getEntityManager().flush();
    		}
    	};
    }
    
    protected void writeReport(AbstractReport<?> report){
    	try {
			IOUtils.write(report.getBytes(), new FileOutputStream( System.getProperty("user.dir")+"/target/"+report.getFileName()+System.currentTimeMillis()+"."+report.getFileExtension()));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
