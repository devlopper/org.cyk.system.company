package org.cyk.system.company.persistence.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.persistence.api.product.ProductDao;
import org.junit.Test;

public class ProductPersistenceIT extends AbstractPersistenceIT {

	private static final long serialVersionUID = -6380442393581625639L;

	@Inject private ProductDao productDao;
	
	@Override
	protected void populate() {
		createProducts(3, 4);
	}
	
	@Override
	protected void queries() {}

	@Test
	public void countAllProduct(){
		assertEquals("Count all products", 7l, productDao.countAll());
	}
	
}
