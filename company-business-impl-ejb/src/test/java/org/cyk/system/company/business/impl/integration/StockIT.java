package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.store.Store;
import org.cyk.system.root.model.store.StoreType;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.junit.Test;

public class StockIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudStockableProductStoreBasedOnExistingProductStoreByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode));
    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).setProductFromCode(tangibleProductCode).setStoreFromCode(storeCode));
    	
    	testCase.create(testCase.instanciateOne(StockableProductStore.class).setProductStoreFromCode(productStoreCode));
    	
    	testCase.clean();
    }
    
    @Test
    public void crudStockableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(StockableTangibleProduct.class).setTangibleProductFromCode(tangibleProductCode));
    	
    	testCase.assertFieldValueEquals(StockableTangibleProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudStockableTangibleProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(StockableTangibleProduct.class).setCode(tangibleProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT)));
    	
    	testCase.assertFieldValueEquals(StockableTangibleProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudStockableTangibleProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String stockableTangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StockableTangibleProduct.class).setCode(stockableTangibleProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT)));
    	
    	testCase.assertNotNull(TangibleProduct.class, stockableTangibleProductCode);
    	testCase.assertNotNull(StockableTangibleProduct.class, stockableTangibleProductCode);
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() { 
			return Arrays.asList(StockableTangibleProduct.class,Movement.class);
		}
		
    }
    
}
