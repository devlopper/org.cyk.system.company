package org.cyk.system.company.business.impl.integration;

import static org.cyk.system.root.model.RootConstant.Code.BusinessRole.PROVIDER;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.information.Comment;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.store.Store;
import org.cyk.system.root.model.store.StoreType;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FileHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class ProductIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudOneProductCategory(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductCategory.class,code));
    	testCase.clean();
    }
    
    @Test
    public void crudOneProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,code));
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductAndImage(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomAlphabetic();
    	FileHelper.File file = RandomHelper.getInstance().getFilePersonHeadOnly(Boolean.TRUE);
    	testCase.create(testCase.instanciateOne(Product.class,code).setImage(inject(FileBusiness.class).process(file.getBytes(), file.getName())));
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductWichIsStockable(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomAlphabetic();
    	testCase.countAll(MovementCollection.class);
    	testCase.create(testCase.instanciateOne(Product.class,code).setStockable(Boolean.TRUE));
    	testCase.assertNotNullByBusinessIdentifier(StockableProduct.class, code);
    	testCase.assertCountAll(MovementCollection.class);
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductWichIsSalable(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,code).setSalable(Boolean.TRUE).setSalableProductPropertiesPriceFromObject(100)
    			.setStockable(Boolean.TRUE).setStockQuantityMovementCollectionInitialValueFromObject(10));
    	SalableProduct salableProduct = testCase.getByIdentifierWhereValueUsageTypeIsBusiness(SalableProduct.class,code,Boolean.TRUE);
    	testCase.assertEqualsNumber(100, salableProduct.getProperties().getPrice());
    	
    	testCase.clean();
    }
    
    /* Store */
    
    @Test
    public void crudOneProductStoreWithExistingProductAndExistingStore(){
    	TestCase testCase = instanciateTestCase(); 
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode));
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).setProductFromCode(productCode).setStoreFromCode(storeCode));
    	
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductStoreWithNoExistingProductAndNoExistingStore(){
    	TestCase testCase = instanciateTestCase(); 
    	    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).addCascadeOperationToMasterFieldNames(ProductStore.FIELD_PRODUCT,ProductStore.FIELD_STORE));
    	
    	testCase.assertNotNullByBusinessIdentifier(Product.class, productStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(Store.class, productStoreCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductStoreBySetStore(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setStorable(Boolean.TRUE).addStoreFromCode(storeCode));
    	
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode));
    	
    	testCase.deleteAll(ProductStore.class);
    	testCase.clean();
    }
    
    @Test
    public void crudProductAndJoinProvider(){
    	TestCase testCase = instanciateTestCase(); 
    	String productProviderCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(Company.class,productProviderCode));
    	
    	String productCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setStockable(Boolean.TRUE).setProviderPartyFromCode(productProviderCode));
    	
    	assertNotNull(inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByIdentifiableGlobalIdentifierByRole(testCase.read(Company.class, productProviderCode)
    			, testCase.read(Product.class, productCode).getGlobalIdentifier(),testCase.read(BusinessRole.class, PROVIDER)));
    	
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductStoreBySetAllStore(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode01).setTypeFromCode(storeTypeCode));
    	
    	String storeCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode02).setTypeFromCode(storeTypeCode));
    	
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setStorable(Boolean.TRUE).addStoresAll());
    	
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode01));
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode02));
    	
    	testCase.deleteAll(ProductStore.class);
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Product.class,Movement.class,Party.class,Store.class,Comment.class);
		}
		
    }
}
