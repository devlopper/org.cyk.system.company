package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.product.SaleBusinessImpl;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.structure.CompanyDao;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.model.party.Application;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Credentials;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.security.License;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.persistence.api.time.TimeDivisionTypeDao;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	//SaleBusinessImpl.DEFAULT_POINT_OF_SALE_REPORT_EXTENSION = "xml";
    	return createRootDeployment();
    } 
    
    @Inject private ApplicationBusiness applicationBusiness;
    @Inject private SaleBusiness saleBusiness;
    @Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject private CashRegisterBusiness cashRegisterBusiness;
    @Inject private ProductDao productDao;
    @Inject private CompanyDao companyDao;
    @Inject private CashierDao cashierDao;
    @Inject private TimeDivisionTypeDao timeDivisionTypeDao;
    
    //private IntangibleProduct product;
    private CashRegister cashRegister1,cashRegisterLimited;
    private Installation installation;
    private Cashier cashier;
    
    @Override
    protected void populate() {
    	installation = new Installation();
    	installation.setAdministratorCredentials(new Credentials("admin", "123"));
    	installation.setApplication(new Application());
    	installation.getApplication().setName("app");
    	installation.setLicense(new License());
    	installation.getLicense().setPeriod(new Period(new Date(), new Date()));
    	installation.setManager(new Person("fn","ln"));
    	installation.setManagerCredentials(new Credentials("man", "123"));
    }
    
    public void acid(){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	saleBusiness.create(sale);
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,new CashRegisterMovement(null, cashRegister1, new BigDecimal("8000"), null),null,null);
    	saleBusiness.create(sale, saleCashRegisterMovement);
    	Assert.assertNotNull(genericBusiness.use(Sale.class).one());
    }
    
    public void oneFullPayment(){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	SaledProduct sp = saleBusiness.selectProduct(sale, productDao.read("prest03"));
    	sp.setQuantity(new BigDecimal("2"));
    	saleBusiness.quantifyProduct(sale, sp);
    	
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	
    	payment(sale, "10000","-1000","10000.00");
    	
    }
    
    public void manyPayments(){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	SaledProduct sp = saleBusiness.selectProduct(sale, productDao.read("prest01"));
    	sp.setQuantity(new BigDecimal("8"));
    	saleBusiness.quantifyProduct(sale, sp);
    	payment(sale, "5000","3000","15000.00");
    	payment(sale, "2000","1000","17000.00");
    	payment(sale, "2000","-1000","19000.00");
    }
    
    public void sell(Date date,String...productIds){
    	Sale sale = new Sale();
    	sale.setDate(date);
    	sale.setCashier(cashier);
    	for(String pid : productIds){
    		saleBusiness.selectProduct(sale, productDao.read(pid));
    	}
    	SaleBusinessImpl.AUTO_SET_SALE_DATE = date!=null;
    	saleBusiness.create(sale, new SaleCashRegisterMovement(sale, new CashRegisterMovement(null, cashRegister1, sale.getCost(), null)));
    	SaleBusinessImpl.AUTO_SET_SALE_DATE = Boolean.TRUE;
    	//payment(sale, "10000","-1000","10000.00");
    	
    }
  
    @Override
    protected void businesses() {
    	
    	applicationBusiness.install(installation);
    	
    	cashier = cashierDao.readAll().iterator().next();
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setCompany(companyDao.readAll().iterator().next());
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	cashRegisterLimited.setCompany(companyDao.readAll().iterator().next());
    	create(cashRegisterLimited);
    	
    	//acid();
    	oneFullPayment();
    	//manyPayments();
    	//for(Sale sale : saleBusiness.findByCriteria(new SaleSearchCriteria()))
    	//	debug(sale);
    	
    	sell(createDate(1, 3, 2015, 1, 0),"prest01");
    	sell(createDate(1, 3, 2015, 1, 0),"prest01");
    	
    	sell(createDate(2, 3, 2015, 1, 0),"prest01");
    	sell(createDate(2, 3, 2015, 1, 0),"prest01");
    	sell(createDate(2, 3, 2015, 1, 0),"prest01");
    	
    	sell(createDate(4, 3, 2015, 1, 0),"prest01");
    
    	//System.out.println(saleBusiness.findStatistics(new SaleSearchCriteria(createDate(1, 3, 2015, 1, 0), createDate(5, 3, 2015, 2, 0)), 
    	//		timeDivisionTypeDao.read(TimeDivisionType.DAY)));
    	
    }

    private void payment(Sale sale,String in,String out,String amount,String balance,String totalBalance){
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
    	saleCashRegisterMovement.setSale(sale); 
    	saleCashRegisterMovement.setAmountIn(new BigDecimal(in==null?amount:in));
    	saleCashRegisterMovement.setAmountOut(new BigDecimal(out==null?balance:out));
    	saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement(null, cashier.getCashRegister(), new BigDecimal(amount), null));
    	
    	if(sale.getIdentifier()==null){
        	saleBusiness.create(sale,saleCashRegisterMovement);	
        	//writeReport(saleBusiness.findReport(Arrays.asList(sale)));
    	}else{
    		saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);	
    	}
    	
    	Assert.assertEquals("Balance",new BigDecimal(balance), sale.getBalance());
    	Assert.assertEquals("Total Balance",new BigDecimal(totalBalance), cashRegisterBusiness.sumBalance());
    		
    }
    
    private void payment(Sale sale,String amount,String balance,String totalBalance){
    	payment(sale, null, null, amount, balance, totalBalance);
    }
    
    private Date createDate(int d,int m,int y,int h,int mm){
		try {
			return DateUtils.parseDate(d+"/"+m+"/"+y+" "+h+":"+mm, "dd/MM/yyyy HH:mm");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
