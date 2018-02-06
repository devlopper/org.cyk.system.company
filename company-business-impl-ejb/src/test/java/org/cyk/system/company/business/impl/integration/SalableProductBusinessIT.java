package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.junit.Test;

public class SalableProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override 
    protected void populate() {
    	create(new TangibleProduct().setCode("TP001").setName("TP 001"));
    	create(new TangibleProduct().setCode("TP002").setName("TP 002"));
    }
        
    //@Test
    public void crudSalableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = new SalableProduct();
    	salableProduct.setProduct(inject(ProductDao.class).readByGlobalIdentifierCode("TP001"));
    	inject(SalableProductBusiness.class).computeChanges(salableProduct);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, "TP001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = new SalableProduct();
    	salableProduct.setCode("TP001");
    	salableProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	salableProduct.setProductClass(TangibleProduct.class);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, "TP001");
    	testCase.clean();
    }
    
    //@Test
    public void crudSalableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	TangibleProduct tangibleProduct = new TangibleProduct();
    	tangibleProduct.setCode("T001");
    	tangibleProduct.setName("My Pro");
    	testCase.create(tangibleProduct);
    	
    	testCase.clean();
    }
}
