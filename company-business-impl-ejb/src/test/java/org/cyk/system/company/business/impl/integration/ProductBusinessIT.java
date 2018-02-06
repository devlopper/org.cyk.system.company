package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.junit.Test;

public class ProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override protected void populate() {}
        
    @Test
    public void crudOneProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	TangibleProduct tangibleProduct = new TangibleProduct();
    	tangibleProduct.setCode("T001");
    	tangibleProduct.setName("My Pro");
    	testCase.create(tangibleProduct);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductCategory(){
    	TestCase testCase = instanciateTestCase(); 
    	ProductCategory productCategory = new ProductCategory();
    	productCategory.setCode("T001");
    	productCategory.setName("My Pro");
    	testCase.create(productCategory);
    	
    	testCase.clean();
    }
}
