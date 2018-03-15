package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.ClassHelper;
import org.junit.Test;

public class ProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
        
    @Test
    public void crudOneTangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	TangibleProduct tangibleProduct = testCase.instanciateOne(TangibleProduct.class,code);
    	testCase.create(tangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,code);
    	testCase.clean();
    }
    
    @Test
    public void crudOneIntangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	IntangibleProduct intangibleProduct = testCase.instanciateOne(IntangibleProduct.class,code);
    	testCase.create(intangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,code);
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductCategory(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	ProductCategory productCategory = testCase.instanciateOne(ProductCategory.class,code);
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
