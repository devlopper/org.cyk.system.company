package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
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
    @Inject private PaymentBusiness paymentBusiness;
    
    private IntangibleProduct product;
    
    @Override
    protected void populate() {
    	product = new IntangibleProduct();
    	product.setCode("prest01");
    	product.setName("Mon produit");
    	product.setPrice(new BigDecimal("8000"));
    	create(product);
    }
    
    public void acid(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	Payment payment = new Payment();
    	payment.setAmountIn(new BigDecimal("10000"));
    	payment.setAmountOut(new BigDecimal("2000"));
    	saleBusiness.create(sale, payment);
    	Assert.assertNotNull(genericBusiness.use(Sale.class).one());
    }
    
    public void oneFullPayment(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	payment(sale, "10000", "500.00","-1500.00");
    }
    
    public void manyPayments(){
    	Sale sale = new Sale();
    	saleBusiness.selectProduct(sale, product);
    	saleBusiness.create(sale);
    	payment(sale, "5000", "0.00","3000.00");
    	payment(sale, "2000", "0.00","1000.00");
    	payment(sale, "2500", "500.00","-1000.00");
    }
  
    @Override
    protected void businesses() {
    	//acid();
    	oneFullPayment();
    	manyPayments();
    	for(Sale sale : saleBusiness.findByCriteria(new SaleSearchCriteria()))
    		debug(sale);
    	
    }

    private void payment(Sale sale,String amountIn,String amountOut,String balance){
    	Payment payment = new Payment();
    	payment.setSale(sale);
    	payment.setAmountIn(new BigDecimal(amountIn));
    	payment.setAmountOut(new BigDecimal(amountOut));
    	
    	paymentBusiness.create(payment);
    	Assert.assertEquals("Balance",new BigDecimal(balance), sale.getBalance());
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
