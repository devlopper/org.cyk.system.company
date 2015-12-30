package org.cyk.system.company.persistence.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class SalePersistenceIT extends AbstractPersistenceIT {
	
	private static final long serialVersionUID = 5955832118708678179L;

	@Deployment
	public static Archive<?> createDeployment() {
	    return createRootDeployment();
	} 
	
	@Inject private SaleDao saleDao;
	private IntangibleProduct product;
		
	@Override
	protected void populate() {
		product = new IntangibleProduct();
    	product.setCode("prest01");
    	product.setName("Mon produit");
    	//product.setPrice(new BigDecimal("1000"));
    	create(product);
    	
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	/*
    	sell("3", "5000", "2000", "3000", Boolean.TRUE);
    	
    	sell("8", "10000", "0", "8000", Boolean.TRUE);
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	sell("1", "1000", "0", "1000", Boolean.TRUE);
    	*/
	}
	
	@Override
	protected void queries() {
	    System.out.println(saleDao.readByCriteria(new SaleSearchCriteria()));
	}
	
	public Sale sell(String quantity,String in,String out,String paid,Boolean soldOut){
		/*
		BigDecimal inb = new BigDecimal(in),outb=new BigDecimal(out);
		BigDecimal cost = new BigDecimal(quantity).multiply(product.getPrice());
		
    	Sale sale = null;//new Sale(null,cost,new Date(),soldOut,null);
    	create(sale);
    	*/
    	/*
    	create(new SaleProduct(sale, product, new BigDecimal("1")));
    	create(new Payment(sale,inb,outb,new BigDecimal(paid),new Date()));
    	*/
    	
    	//pay(sale, in, out, paid, soldOut);
    	//return sale;
		return null;
    }
	
	/*
	public SaleCashRegisterMovement pay(Sale sale,String in,String out,String paid,Boolean soldOut){
		//sale.setSoldOut(soldOut);
		BigDecimal inb = new BigDecimal(in),outb=new BigDecimal(out);
		SaleCashRegisterMovement payment = null;//new Payment(sale,inb,outb,new BigDecimal(paid),new Date());
    	create(payment);
    	return payment;
    }*/
					
	// CRUD 
	
	@Override
	protected void create() {}

	@Override
	protected void read() {}

	@Override
	protected void update() {}

	@Override
	protected void delete() {}
	
	
	
}
