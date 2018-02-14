package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
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
    public void crudOneTangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	TangibleProduct tangibleProduct = inject(TangibleProductBusiness.class).instanciateOne();
    	tangibleProduct.setCode("TP001");
    	tangibleProduct.setName("My tang");
    	testCase.create(tangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,"TP001");
    	testCase.clean();
    }
    
    @Test
    public void crudOneIntangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	IntangibleProduct intangibleProduct = inject(IntangibleProductBusiness.class).instanciateOne();
    	intangibleProduct.setCode("IP001");
    	intangibleProduct.setName("My int");
    	testCase.create(intangibleProduct);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductCategory(){
    	TestCase testCase = instanciateTestCase(); 
    	ProductCategory productCategory = inject(ProductCategoryBusiness.class).instanciateOne();
    	productCategory.setCode("PC001");
    	productCategory.setName("My categ");
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
