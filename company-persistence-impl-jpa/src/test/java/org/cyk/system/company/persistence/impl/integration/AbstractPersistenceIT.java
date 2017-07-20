package org.cyk.system.company.persistence.impl.integration;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.Getter;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.impl.accounting.CompanyPersistenceTestHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.root.persistence.impl.AbstractPersistenceService;
import org.cyk.system.root.persistence.impl.PersistenceIntegrationTestHelper;
import org.cyk.utility.test.ArchiveBuilder;
import org.cyk.utility.test.TestMethod;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

/**
 * Persistence integration test (IT)
 * @author Komenan Y .Christian
 *
 */ 
public abstract class AbstractPersistenceIT extends AbstractIntegrationTestJpaBased {
	 
	private static final long serialVersionUID = -3977685343817022628L;

	@Deployment
	public static Archive<?> createDeployment() {
	    return createRootDeployment();
	} 
	
	@Inject @Getter private GenericDao genericDao;
	@Inject CompanyPersistenceTestHelper companyPersistenceTestHelper;
	
	@Override
	protected void populate() {
		
	}
	
	@Override
    protected void _execute_() {
        super._execute_();
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { create(); } });    
        read(); 
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { update(); } });    
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { delete(); } });    
        queries();
    }
	
	/**/
	
	@Override
	public EntityManager getEntityManager() {
	    return ((AbstractPersistenceService<?>)genericDao).getEntityManager();
	}
	
	protected abstract void queries();
	
	/* Shortcut */
	
	protected AbstractIdentifiable create(AbstractIdentifiable object){
		return genericDao.create(object);
	}
	
	protected AbstractIdentifiable update(AbstractIdentifiable object){
		return genericDao.update(object);
	}
	
	@Override protected void create() {}
	@Override protected void read() {}
	@Override protected void update() {}
	@Override protected void delete() {}
	
	protected void createProducts(Integer tangibleProductCount,Integer intangibleProductCount) {
		for(int i=1;i<=tangibleProductCount;i++){
			TangibleProduct tangibleProduct = new TangibleProduct();
			companyPersistenceTestHelper.set(tangibleProduct, "TP"+i);
			create(tangibleProduct);
		}
		
		for(int i=1;i<=intangibleProductCount;i++){
			IntangibleProduct intangibleProduct = new IntangibleProduct();
			companyPersistenceTestHelper.set(intangibleProduct, "IP"+i);
			create(intangibleProduct);
		}
	}
	
	protected void createSales(String[][] salableProducts,String[][] sales) {
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setExistencePeriod(new Period(new Date(), new Date()));
		create(accountingPeriod);
		
		Person person = new Person();
		person.setCode("p001");
		person.setContactCollection(null);
		person.setName("name");
		create(person);
		
		//CashRegister cashRegister = new ;
		
		//Cashier cashier = new Cashier(person, cashRegister);
		
		createProducts(3, 4);
		
		for(String[] infos : salableProducts){
			SalableProduct salableProduct = new SalableProduct();
			companyPersistenceTestHelper.set(salableProduct, infos[0], infos[1]);
			create(salableProduct);
		}
		
		for(String[] infos : sales){
			Sale sale = new Sale();
			companyPersistenceTestHelper.set(sale, infos[0], infos[1], infos[2]);
			create(sale);
		}
	}
	
	public static Archive<?> createRootDeployment() {
        return  
                new ArchiveBuilder().create().getArchive().
                    addClasses(PersistenceIntegrationTestHelper.classes()).
                    addPackages(Boolean.TRUE,"org.cyk.system.company") 
                    
                ;
    } 
	
}
