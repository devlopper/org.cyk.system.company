package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;

public class StockBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
     
    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
       
    @Override
    protected void businesses() {
    	installApplication();
    	assertStockMovement("tprod01", "5", "5");
    	assertStockMovement("tprod01", "9", "14");
    	assertStockMovement("tprod01", "10", "24");
    	assertStockMovement("tprod01", "-5", "19");
    }
    
    @Test(expected=Exception.class)
    public void noZeroMovement(){
    	assertStockMovement("tprod02", "0", "0");
    }
    
    @Test(expected=Exception.class)
    public void noNegativeStock(){
    	assertStockMovement("tprod02", "-5", "-5");
    }
    
    private void assertStockMovement(String tpCode,String quantity,String totalStock){
    	TangibleProduct tangibleProduct = (TangibleProduct) productDao.read(tpCode);
    	tangibleProductStockMovementBusiness.create(new TangibleProductStockMovement(tangibleProduct, null, new BigDecimal(quantity), null));
    	Assert.assertEquals(new BigDecimal(totalStock), tangibleProduct.getStockQuantity());
    }

    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
}
