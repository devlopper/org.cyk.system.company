package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.business.impl__data__.DataSet;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.ClassHelper;
import org.junit.Test;

public class ProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
        
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
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Product.class,Movement.class);
		}
		
    }
}
