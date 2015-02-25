package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
        return createRootDeployment();
    } 
    
    @Inject private SaleBusiness saleBusiness;
    @Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject private CashRegisterBusiness cashRegisterBusiness;
    
    private IntangibleProduct product;
    private CashRegister cashRegister1,cashRegisterLimited;
    
    @Override
    protected void populate() {
    	product = new IntangibleProduct();
    	product.setCode("prest01");
    	product.setName("Mon produit");
    	product.setPrice(new BigDecimal("8000"));
    	create(product);
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setName("Cashier001");
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setName("Cashier002");
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	create(cashRegisterLimited);
    }
    
    public void acid(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,new CashRegisterMovement(null, cashRegister1, new BigDecimal("8000"), null));
    	saleBusiness.create(sale, saleCashRegisterMovement);
    	Assert.assertNotNull(genericBusiness.use(Sale.class).one());
    }
    
    public void oneFullPayment(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	payment(cashRegister1,sale, "10000","-2000","10000.00");
    }
    
    public void manyPayments(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	payment(cashRegister1,sale, "5000","3000","15000.00");
    	payment(cashRegister1,sale, "2000","1000","17000.00");
    	payment(cashRegister1,sale, "2000","-1000","19000.00");
    }
  
    @Override
    protected void businesses() {
    	//acid();
    	oneFullPayment();
    	manyPayments();
    	//for(Sale sale : saleBusiness.findByCriteria(new SaleSearchCriteria()))
    	//	debug(sale);
    	
    }

    private void payment(CashRegister cashRegister,Sale sale,String amount,String balance,String totalBalance){
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
    	saleCashRegisterMovement.setSale(sale); 
    	saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement(null, cashRegister, new BigDecimal(amount), null));
    	saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
    	Assert.assertEquals("Balance",new BigDecimal(balance), sale.getBalance());
    	Assert.assertEquals("Total Balance",new BigDecimal(totalBalance), cashRegisterBusiness.sumBalance());
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
