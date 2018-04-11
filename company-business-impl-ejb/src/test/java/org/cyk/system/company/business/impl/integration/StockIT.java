package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.movement.Movement;
import org.cyk.system.root.model.store.Store;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.junit.Test;

public class StockIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudStockableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(StockableProduct.class).setProductFromCode(productCode));
    	
    	testCase.assertFieldValueEquals(StockableProduct.class, productCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudStockableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(StockableProduct.class).setCode(productCode)
    			.addCascadeOperationToMasterFieldNames(StockableProduct.FIELD_PRODUCT));
    	
    	testCase.assertFieldValueEquals(StockableProduct.class, productCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudStockableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String stockableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StockableProduct.class).setCode(stockableProductCode)
    			.addCascadeOperationToMasterFieldNames(StockableProduct.FIELD_PRODUCT));
    	
    	testCase.assertNotNullByBusinessIdentifier(Product.class, stockableProductCode);
    	testCase.assertNotNullByBusinessIdentifier(StockableProduct.class, stockableProductCode);
    	testCase.clean();
    }
    
    /* Store */
    
    @Test
    public void crudStockableProductStoreBasedOnNoExistingProductStoreByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).addCascadeOperationToMasterFieldNames(ProductStore.FIELD_PRODUCT
    			,ProductStore.FIELD_STORE).setProductStockable(Boolean.TRUE));
    	
    	testCase.assertNotNullByBusinessIdentifier(Product.class, productStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(Store.class, productStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(StockableProductStore.class, productStoreCode);
    	
    	testCase.deleteAll(StockableProductStore.class);
    	
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() { 
			return Arrays.asList(StockableProduct.class,Movement.class,Store.class);
		}
		
    }
    
}
