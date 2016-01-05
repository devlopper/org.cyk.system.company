package org.cyk.system.company.persistence.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.junit.Test;

public class SalePersistenceIT extends AbstractPersistenceIT {

	private static final long serialVersionUID = -6380442393581625639L;

	@Inject private SalableProductDao salableProductDao;
	@Inject private SaleDao saleDao;
	
	@Override
	protected void populate() {
		createSales(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"IP2", "700"} } , new String[][]{ 
				{"1000","0","0"},{"750","0","0"},{"1500","0","0"},{"800","0","0"}
		});
	}
	
	@Override
	protected void queries() {}

	@Test
	public void countAllSalableProduct(){
		assertEquals("Count all salable products", 3l, salableProductDao.countAll());
	}
	
	@Test
	public void countAllSale(){
		assertEquals("Count all sale", 4l, saleDao.countAll());
	}
	
	@Test
	public void countBySaleSearchCriteria(){
		SaleSearchCriteria criteria = new SaleSearchCriteria();
		assertEquals("Count all sale by search criteria", 4l, saleDao.countByCriteria(criteria));
	}
	
}
