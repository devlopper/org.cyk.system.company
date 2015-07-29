package org.cyk.system.company.business.impl.integration;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.MatcherAssert.*;

import org.apache.commons.io.IOUtils;
import org.cyk.system.company.business.api.CompanyBusinessTestHelper;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootTestHelper;
import org.cyk.system.root.business.impl.validation.AbstractValidator;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.AbstractReport;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.utility.test.ArchiveBuilder;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.jboss.shrinkwrap.api.Archive;

import net.sf.jasperreports.view.JasperViewer;

public abstract class AbstractBusinessIT extends AbstractIntegrationTestJpaBased {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Inject protected ApplicationBusiness applicationBusiness;
	@Inject protected ExceptionUtils exceptionUtils;
	@Inject protected DefaultValidator defaultValidator;
	@Inject private GenericDaoImpl g;
	@Inject protected GenericBusiness genericBusiness;
	@Inject protected ProductDao productDao;
	@Inject protected RootTestHelper rootTestHelper;

	@Inject protected ValidatorMap validatorMap;// = ValidatorMap.getInstance();
	@Inject protected RootBusinessLayer rootBusinessLayer;
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
    	
    }
    
    protected SaleStockInput drop(Date date,Person person,Customer customer,String externalIdentifier,String cost,String commission,String quantity,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
    	SaleStockInput saleStockInput = saleStockInputBusiness.newInstance(person);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person);
    	companyBusinessTestHelper.set(saleStockInput, customerBusiness.load(customer.getIdentifier()),externalIdentifier, cost, commission, quantity,date);
    	companyBusinessTestHelper.set(saleCashRegisterMovement, "0");
    	saleStockInputBusiness.create(saleStockInput,saleCashRegisterMovement);
    	
    	saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
    	assertBigDecimalValue("Cost", expectedCost, saleStockInput.getSale().getCost());
    	assertBigDecimalValue("VAT", expectedVat, saleStockInput.getSale().getValueAddedTax());
    	assertBigDecimalValue("Balance", expectedBalance, saleStockInput.getSale().getBalance().getValue());
    	assertBigDecimalValue("Cumul Balance", expectedCumulBalance, saleStockInput.getSale().getBalance().getCumul());
    	return saleStockInput;
    }
    
    protected void taking(Date date,Person person,SaleStockInput saleStockInput,String quantity,String paid,String expectedRemainingGoods,String expectedBalance,String expectedCumulBalance){
    	saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
    	SaleStockOutput saleStockOutput = saleStockOutputBusiness.newInstance(person, saleStockInput);
    	companyBusinessTestHelper.set(saleStockOutput, quantity);
    	companyBusinessTestHelper.set(saleStockOutput.getSaleCashRegisterMovement(), paid,date);
    	saleStockOutputBusiness.create(saleStockOutput);
    	
    	saleStockOutput = saleStockOutputBusiness.load(saleStockOutput.getIdentifier());
    	//Matchers
    	assertBigDecimalValue("Remaining number of goods", expectedRemainingGoods, saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods());
    	assertBigDecimalValue("Balance", expectedBalance, saleStockOutput.getSaleStockInput().getSale().getBalance().getValue());
    	assertBigDecimalValue("Cumul Balance", expectedCumulBalance, saleStockOutput.getSaleCashRegisterMovement().getBalance().getCumul());
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
    
    protected void installApplication(){
    	companyBusinessLayer.installApplication();
    }
    
    protected void validate(Object object){
        if(object==null)
            return;
        @SuppressWarnings("unchecked")
        AbstractValidator<Object> validator = (AbstractValidator<Object>) validatorMap.validatorOf(object.getClass());
        if(validator==null){
            //log.warning("No validator has been found. The default one will be used");
            //validator = defaultValidator;
            return;
        }
        try {
            validator.validate(object);
        } catch (Exception e) {}
        
        if(!Boolean.TRUE.equals(validator.isSuccess()))
            System.out.println(validator.getMessagesAsString());
        
    }
    
    protected void jasperViewer(final byte[] bytes){
    	Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				JasperViewer jasperViewer;
				try {
					jasperViewer = new JasperViewer(new ByteArrayInputStream(bytes),Boolean.TRUE);
					
					jasperViewer.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	
    	thread.run();
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
                    addPackages(Boolean.FALSE, BusinessIntegrationTestHelper.packages()).
                    addPackages(Boolean.TRUE,"org.cyk.system.company") 
                    .addClasses(RootBusinessLayer.class,CompanyBusinessLayer.class)
                    
                ;
    } 
}
