package org.cyk.system.company.persistence.impl.integration;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class SaleComputationsPersistenceIT extends AbstractPersistenceIT {
	
	private static final long serialVersionUID = 5955832118708678179L;

	@Deployment
	public static Archive<?> createDeployment() {
	    return createRootDeployment();
	} 
	
	@Inject private SaleDao saleDao;
		
	@Override
	protected void populate() {
		sale("1200", "1000", "200","500");
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
	
	private void sale(String cost,String turnover,String tax,String balance){
		Sale sale = new Sale();
		sale.setCost(new BigDecimal(cost));
		sale.setTurnover(new BigDecimal(turnover));
		sale.setValueAddedTax(new BigDecimal(tax));
		sale.getBalance().setValue(new BigDecimal(balance));
		sale.setDate(new Date());
		debug(sale);
		create(sale);
	}
					
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
