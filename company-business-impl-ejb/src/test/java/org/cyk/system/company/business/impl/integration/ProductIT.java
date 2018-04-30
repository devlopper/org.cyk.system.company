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
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.information.Comment;
import org.cyk.system.root.model.language.programming.Script;
import org.cyk.system.root.model.mathematics.movement.Movement;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.metadata.Entity;
import org.cyk.system.root.model.metadata.EntityProperty;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.persistence.api.metadata.EntityDao;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
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
    	testCase.create(testCase.instanciateOne(Product.class,code).addCascadeOperationToMasterFieldNames(FieldHelper.getInstance().buildPath(Product.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_IMAGE))
    			.setImage(inject(FileBusiness.class).process(file.getBytes(), file.getName())));
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductWithCodeScript_nameTwoFirst_categoryTwoFirstLetter_orderNumber(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String productCategory01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductCategory.class,productCategory01).setName("Métallique"));
    	String productCategory02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductCategory.class,productCategory02).setName("Non Métallique"));
    	System.out.println("ProductIT.crudOneProductWithCodeScript_nameTwoFirst_categoryTwoFirstLetter_orderNumber() : "+inject(EntityDao.class).readAll());
    	String scriptCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Script.class,scriptCode).setText("instance.name.substring(0,2).toUpperCase()+''+instance.category.name.substring(0,2).toUpperCase()"));
    	String entityPropertyCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(EntityProperty.class,entityPropertyCode).setEntityFromCode(RootConstant.Code.Entity.PRODUCT)
    			.setPropertyFromCode(RootConstant.Code.Property.CODE).setValueGeneratorScriptFromCode(scriptCode));
    	
    	String productCode = testCase.create(testCase.instanciateOne(Product.class).setName("WOODIN").setCategoryFromCode(productCategory01)).getCode();
    	testCase.assertEquals("WOMÉ", productCode);
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
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode));
    	
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
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode));
    	
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
    	
    	String storeCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode01));
    	
    	String storeCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode02));
    	
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setStorable(Boolean.TRUE).addStoresAll());
    	
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode01));
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode02));
    	
    	testCase.deleteAll(ProductStore.class);
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductStoreBySetManyStore(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String storeCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode01).setHasPartyAsCompany(Boolean.TRUE));
    	
    	String storeCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode02).setHasPartyAsCompany(Boolean.TRUE));
    	
    	String productCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Product.class,productCode).setStorable(Boolean.TRUE).addStoresFromCode(storeCode01,storeCode02)
    			.setSalable(Boolean.TRUE).setStockable(Boolean.TRUE));
    	
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode01));
    	testCase.assertNotNullByBusinessIdentifier(ProductStore.class, RootConstant.Code.generate(productCode,storeCode02));
    	
    	testCase.deleteAll(SalableProductStore.class);
    	testCase.deleteAll(StockableProductStore.class);
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
			return Arrays.asList(Product.class,Movement.class,Party.class,Comment.class,Entity.class,Script.class);
		}
		
    }
}
