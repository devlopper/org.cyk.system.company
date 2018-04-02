package org.cyk.system.company.business.impl.integration;

import static org.cyk.system.root.model.RootConstant.Code.BusinessRole.PROVIDER;
import static org.cyk.system.root.model.RootConstant.Code.MovementCollectionType.SALE_BALANCE;
import static org.cyk.system.root.model.RootConstant.Code.MovementCollectionType.STOCK_REGISTER;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.business.impl.__test__.Runnable;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductProperties;
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionItemDao;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.information.IdentifiableCollectionBusiness;
import org.cyk.system.root.business.api.information.IdentifiableCollectionItemBusiness;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.information.IdentifiableCollection;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.MovementCollectionType;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.store.Store;
import org.cyk.system.root.model.store.StoreType;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionDao;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.ConditionHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.FileHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class SaleIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;

    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    /* SalableProduct */
    
    @Test
    public void crudSalableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setProductFromCode(tangibleProductCode));
    	
    	testCase.assertFieldValueEquals(SalableProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	
    	testCase.assertNotNullByBusinessIdentifier(SalableProductProperties.class, tangibleProductCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(tangibleProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class));
    	
    	testCase.assertFieldValueEquals(SalableProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class));
    	
    	testCase.assertNotNullByBusinessIdentifier(TangibleProduct.class, salableProductCode);
    	testCase.assertNotNullByBusinessIdentifier(SalableProduct.class, salableProductCode);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnNonExistingProductByCodeWithImage(){
    	TestCase testCase = instanciateTestCase(); 
    	String salableProductCode = testCase.getRandomAlphabetic();
    	FileHelper.File file = RandomHelper.getInstance().getFilePersonHeadOnly(Boolean.TRUE);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setImage(inject(FileBusiness.class).process(file.getBytes(), file.getName()))
    			);
    	
    	testCase.deleteByCode(SalableProduct.class, salableProductCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductByCodeAndJoinProviderAndStock(){
    	TestCase testCase = instanciateTestCase(); 
    	String productProviderCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Company.class,productProviderCode));
    	
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setProductIsStockable(Boolean.TRUE)
    			.setProductProviderPartyFromCode(productProviderCode)
    			);
    	
    	testCase.assertNotNullByBusinessIdentifier(TangibleProduct.class, salableProductCode);
    	testCase.assertNotNullByBusinessIdentifier(SalableProduct.class, salableProductCode);
    	testCase.assertNotNullByBusinessIdentifier(StockableProduct.class, salableProductCode);
    	assertNotNull(inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByIdentifiableGlobalIdentifierByRole(testCase.read(Company.class, productProviderCode)
    			, testCase.read(TangibleProduct.class, salableProductCode).getGlobalIdentifier(),testCase.read(BusinessRole.class, PROVIDER)));
    	
    	testCase.clean();
    }
    
    /* SalableProductStore */
    
    @Test
    public void crudSalableProductStoreBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String tangibleProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode));
    	
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode).setProductClass(TangibleProduct.class).setProductFromCode(tangibleProductCode));
    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).setProductFromCode(tangibleProductCode).setStoreFromCode(storeCode));
    	
    	String salableProductStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode).setProductStoreFromCode(productStoreCode));
    	
    	testCase.assertNotNullByBusinessIdentifier(SalableProductProperties.class, salableProductCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreBasedOnNoExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	
    	String salableProductStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode)
    			.addCascadeOperationToMasterFieldNames(SalableProductStore.FIELD_PRODUCT_STORE,SalableProductStore.FIELD_SALABLE_PRODUCT_PROPERTIES)
    			.addProductStoreCascadeOperationToMasterFieldNames(ProductStore.FIELD_PRODUCT,ProductStore.FIELD_STORE)
    			.setProductStoreProductClass(TangibleProduct.class).setProductStoreProductIsStockable(Boolean.TRUE));
    	
    	testCase.assertNotNullByBusinessIdentifier(SalableProductProperties.class, salableProductStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(TangibleProduct.class, salableProductStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(Store.class, salableProductStoreCode);
    	testCase.assertNotNullByBusinessIdentifier(StockableProductStore.class, salableProductStoreCode);
    	
    	testCase.clean();
    }
    
    /* SalableProductCollection */
    
    @Test
    public void crudSalableProductCollection(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode));
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "0");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,null,null,null,null);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE));
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "0");    	
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"0","0","0","0");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithOneItem(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductCollection salableProductCollection = testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode)
    			.setCostNumberOfProceedElements(2));
    	
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE)
    			.add(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	testCase.update(salableProductCollection);
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")));
    	
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductCollection salableProductCollection = testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode01)
    			.setCostNumberOfProceedElements(2),testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode02)
    			.setCostNumberOfProceedElements(3));
    	
    	testCase.create(salableProductCollection);
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "2");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"5","650","100","550");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")));
    	
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductCollection salableProductCollection = testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode01)
    			.setCostNumberOfProceedElements(2));
    	
    	testCase.create(salableProductCollection);
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE)
    			.add(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode02)
    			.setCostNumberOfProceedElements(1));
    	
    	testCase.update(salableProductCollection);
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "2");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"3","350","54","296");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void computeSalableProductCollectionChanges(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")));
    	
    	SalableProductCollection salableProductCollection = testCase.instanciateOne(SalableProductCollection.class).setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	testCase.computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode01)
    			.setCostNumberOfProceedElements(2));
    	
    	testCase.computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(), "2","200","31","169");
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode02)
    			.setCostNumberOfProceedElements(1));
    	
    	testCase.computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(),"3","350","54","296");
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode02)
    			.setCostNumberOfProceedElements(-1));
    	
    	testCase.computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(), "2","200","31","169");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemWithProductQuantityUpdatable(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setPriceFromObject(100).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProductFromCode(salableProductCode);
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	testCase.assertStockableProduct(salableProductCode, "8");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode);
    	salableProductCollection.getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	testCase.assertStockableProduct(salableProductCode, "8");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemWithProductQuantityUpdatableExceedUpperLimit(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setPriceFromObject(100).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductCollectionCode = testCase.getRandomAlphabetic();
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("11"));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    /* SalableProductStoreCollection */
    
    @Test
    public void crudSalableProductStoreCollection(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStoreCollection.class, salableProductStoreCollectionCode));
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "0");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost(), salableProductStoreCollectionCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreCollectionWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStoreCollection.class, salableProductStoreCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE));
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "0");    	
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(0).setValueFromObject(0)
    			.setTaxFromObject(0).setTurnoverFromObject(0), salableProductStoreCollectionCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreCollectionWithOneItem(){
    	TestCase testCase = instanciateTestCase();
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(100));
    	
    	String productStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode).setProductFromCode(salableProductCode).setStoreFromCode(storeCode));
    	
    	String salableProductStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode).setProductStoreFromCode(productStoreCode));
    	
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductStoreCollection salableProductStoreCollection = testCase.instanciateOne(SalableProductStoreCollection.class, salableProductStoreCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductStoreCollection.add(testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode)
    			.setCostNumberOfProceedElements(2));
    	
    	testCase.create(salableProductStoreCollection);
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "1");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(2).setValueFromObject(200)
    			.setTaxFromObject(31).setTurnoverFromObject(169), salableProductStoreCollectionCode);
    	
    	salableProductStoreCollection = testCase.read(SalableProductStoreCollection.class, salableProductStoreCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE)
    			.add(inject(SalableProductStoreCollectionItemDao.class).readByCollection(salableProductStoreCollection));
    	testCase.update(salableProductStoreCollection);
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "1");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(2).setValueFromObject(200)
    			.setTaxFromObject(31).setTurnoverFromObject(169), salableProductStoreCollectionCode);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreCollectionWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode01).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(100));
    	
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode02).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(150));
    	
    	String productStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode01).setProductFromCode(salableProductCode01).setStoreFromCode(storeCode));
    	
    	String productStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode02).setProductFromCode(salableProductCode02).setStoreFromCode(storeCode));
    	
    	String salableProductStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode01).setProductStoreFromCode(productStoreCode01));
    	
    	String salableProductStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode02).setProductStoreFromCode(productStoreCode02));
    	
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductStoreCollection salableProductStoreCollection = testCase.instanciateOne(SalableProductStoreCollection.class, salableProductStoreCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductStoreCollection.add(
    			testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode01).setCostNumberOfProceedElements(2)
    			,testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode02).setCostNumberOfProceedElements(3)
    			);
    	
    	testCase.create(salableProductStoreCollection);
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "2");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(5)
    			.setValueFromObject(650).setTaxFromObject(100).setTurnoverFromObject(550),salableProductStoreCollectionCode);
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreCollectionWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode01).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(100));
    	
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode02).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(150));
    	
    	String productStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode01).setProductFromCode(salableProductCode01).setStoreFromCode(storeCode));
    	
    	String productStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode02).setProductFromCode(salableProductCode02).setStoreFromCode(storeCode));
    	
    	String salableProductStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode01).setProductStoreFromCode(productStoreCode01));
    	
    	String salableProductStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode02).setProductStoreFromCode(productStoreCode02));
    	
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	SalableProductStoreCollection salableProductStoreCollection = testCase.instanciateOne(SalableProductStoreCollection.class, salableProductStoreCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductStoreCollection.add(
    			testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode01).setCostNumberOfProceedElements(2)
    			);
    	
    	testCase.create(salableProductStoreCollection);
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "1");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(2)
    			.setValueFromObject(200).setTaxFromObject(31).setTurnoverFromObject(169),salableProductStoreCollectionCode);
    	
    	salableProductStoreCollection = testCase.read(SalableProductStoreCollection.class, salableProductStoreCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE)
    			.add(inject(SalableProductStoreCollectionItemDao.class).readByCollection(salableProductStoreCollection));
    	salableProductStoreCollection.add(
    			testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode02).setCostNumberOfProceedElements(1)
    			);
    	
    	testCase.update(salableProductStoreCollection);
    	
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "2");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(3)
    			.setValueFromObject(350).setTaxFromObject(54).setTurnoverFromObject(296),salableProductStoreCollectionCode);
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void computeSalableProductStoreCollectionChanges(){
    	TestCase testCase = instanciateTestCase();
    	
    	String storeTypeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(StoreType.class,storeTypeCode));
    	
    	String storeCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(Store.class,storeCode).setTypeFromCode(storeTypeCode));
    	
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode01).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(100));
    	
    	String salableProductCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class,salableProductCode02).addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT)
    			.setProductClass(TangibleProduct.class).setPropertiesPriceFromObject(150));
    	
    	String productStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode01).setProductFromCode(salableProductCode01).setStoreFromCode(storeCode));
    	
    	String productStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(ProductStore.class,productStoreCode02).setProductFromCode(salableProductCode02).setStoreFromCode(storeCode));
    	
    	String salableProductStoreCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode01).setProductStoreFromCode(productStoreCode01));
    	
    	String salableProductStoreCode02 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode02).setProductStoreFromCode(productStoreCode02));
    	
    	SalableProductStoreCollection salableProductStoreCollection = testCase.instanciateOne(SalableProductStoreCollection.class).setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	testCase.computeChanges(salableProductStoreCollection);
    	testCase.assertCost(salableProductStoreCollection.getCost(), "0", "0", "0", "0");
    	
    	salableProductStoreCollection.add(testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode01)
    			.setCostNumberOfProceedElements(2));
    	
    	testCase.computeChanges(salableProductStoreCollection);
    	testCase.assertCost(salableProductStoreCollection.getCost(), "2","200","31","169");
    	
    	salableProductStoreCollection.add(testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode02)
    			.setCostNumberOfProceedElements(1));
    	
    	testCase.computeChanges(salableProductStoreCollection);
    	testCase.assertCost(salableProductStoreCollection.getCost(),"3","350","54","296");
    	
    	salableProductStoreCollection.add(testCase.instanciateOne(SalableProductStoreCollectionItem.class).setSalableProductStoreFromCode(salableProductStoreCode02)
    			.setCostNumberOfProceedElements(-1));
    	
    	testCase.computeChanges(salableProductStoreCollection);
    	testCase.assertCost(salableProductStoreCollection.getCost(), "2","200","31","169");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductStoreCollectionWithItemWithProductQuantityUpdatable(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductStoreCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProductStore.class,salableProductStoreCode)
    			.addCascadeOperationToMasterFieldNames(SalableProductStore.FIELD_PRODUCT_STORE,SalableProductStore.FIELD_SALABLE_PRODUCT_PROPERTIES)
    			.addProductStoreCascadeOperationToMasterFieldNames(ProductStore.FIELD_PRODUCT,ProductStore.FIELD_STORE)
    			.setProductStoreProductClass(TangibleProduct.class).setProductStoreProductIsStockable(Boolean.TRUE)
    			.setSalableProductPropertiesPriceFromObject(100).setSalableProductPropertiesProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	SalableProductStoreCollection salableProductStoreCollection = inject(SalableProductStoreCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	salableProductStoreCollection.setCode(salableProductStoreCollectionCode);
    	SalableProductStoreCollectionItem salableProductStoreCollectionItem = inject(SalableProductStoreCollectionItemBusiness.class).instanciateOne(salableProductStoreCollection);
    	salableProductStoreCollectionItem.setSalableProductStoreFromCode(salableProductStoreCode);
    	salableProductStoreCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	salableProductStoreCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductStoreCollection);
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "1");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(2)
    			.setValueFromObject(200).setTaxFromObject(31).setTurnoverFromObject(169),salableProductStoreCollectionCode);
    	testCase.assertStockableProductStore(salableProductStoreCode, "8");
    	
    	salableProductStoreCollection = testCase.read(SalableProductStoreCollection.class, salableProductStoreCollectionCode);
    	salableProductStoreCollection.getItems().addMany(inject(SalableProductStoreCollectionItemDao.class).readByCollection(salableProductStoreCollection));
    	salableProductStoreCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(salableProductStoreCollection);
    	testCase.assertCollection(SalableProductStoreCollection.class, SalableProductStoreCollectionItem.class, salableProductStoreCollectionCode, "1");
    	testCase.assertEqualsSalableProductStoreCollectionCost(new Cost().setNumberOfProceedElementsFromObject(2)
    			.setValueFromObject(200).setTaxFromObject(31).setTurnoverFromObject(169),salableProductStoreCollectionCode);
    	
    	testCase.assertStockableProductStore(salableProductStoreCode, "8");
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    
    /*@Test
    public void crudSalableProductStoreCollectionWithItemWithProductQuantityUpdatableExceedUpperLimit(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setPriceFromObject(100).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	SalableProductStoreCollection salableProductStoreCollection = inject(SalableProductStoreCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductStoreCollectionCode = testCase.getRandomAlphabetic();
    	salableProductStoreCollection.setCode(salableProductStoreCollectionCode);
    	SalableProductStoreCollectionItem salableProductStoreCollectionItem = inject(SalableProductStoreCollectionItemBusiness.class).instanciateOne(salableProductStoreCollection);
    	salableProductStoreCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode));
    	salableProductStoreCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("11"));
    	salableProductStoreCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductStoreCollection);
    	
    	testCase.deleteAll(StockableProduct.class);
    	testCase.clean();
    }
    */
    /* Sale */
    
    @Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	testCase.create(sale);
    	assertNotNull(sale.getCode());
    	testCase.assertNotNullByBusinessIdentifier(MovementCollection.class, RootConstant.Code.generate(sale.getCode(),SALE_BALANCE));
    	testCase.assertSaleCost(sale.getCode(),null,null,null,null);
    	testCase.clean();
    	testCase.assertNullByBusinessIdentifier(MovementCollection.class, RootConstant.Code.generate(sale.getCode(),SALE_BALANCE));
    }
    
    @Test
    public void crudSaleWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertSalableProductCollection(saleCode,"0","0","0","0");
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItem(){
    	TestCase testCase = instanciateTestCase();
    	testCase.countAll(Movement.class);
    	testCase.addClasses(SalableProductCollection.class,SalableProductCollectionItem.class);
    	
    	String salableProductCode = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	testCase.assertStockableProduct(salableProductCode, "8");
    	//testCase.assertmovem
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	testCase.assertStockableProduct(salableProductCode, "8");
    	
    	testCase.clean();
    	testCase.assertCountAll(Movement.class, 2);
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    }
    
    @Test
    public void crudSaleWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	String salableProductCode03 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode03).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode03));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("1"));
    	
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "2");
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    }
    
    @Test
    public void crudSaleWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	String salableProductCode03 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode03).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode03));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("1"));
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "2");
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    }
    
    @Test
    public void computeSaleChanges(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	String salableProductCode03 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode03).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "0", "0", "0", "0");
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "2","200","31","169");
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode03));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("1"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(),"3","350","54","296");
    	
    	testCase.clean();
    	testCase.deleteAll(StockableProduct.class);
    }
    
    @Test
    public void crudSaleWithItemWithProductQuantityVariation(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("4"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableProduct(salableProductCode01, "6");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "6", "1");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().getElements().iterator().next().getCost().setNumberOfProceedElements(new BigDecimal("3"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertStockableProduct(salableProductCode01, "7");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "7", "2");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().removeAll();
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertStockableProduct(salableProductCode01, "10");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "10", "3");
    	
    	testCase.clean();
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "10", "3");
    	testCase.assertMovements(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER)
    		, new String[]{"0","-4","6","false"}
    		, new String[]{"1","1","7","true"}
    		, new String[]{"2","3","10","true"});
    	
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    }
    
    @Test
    public void crudSaleWithItemWithBalanceVariation(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("4"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableProduct(salableProductCode01, "6");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "400", "0");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().getElements().iterator().next().getCost().setNumberOfProceedElements(new BigDecimal("3"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertStockableProduct(salableProductCode01, "7");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "7", "2");
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "300", "1");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().removeAll();
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertStockableProduct(salableProductCode01, "10");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "10", "3");
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "0", "2");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-100","300","false"}
        		, new String[]{"1","-300","0","false"});
    	testCase.clean();
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "10", "3");  	
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    	
    }
    
    @Test
    public void crudSaleWithItemWithBalanceVariationPayment(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = testCase.getRandomAlphabetic();
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PRODUCT).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")).setProductIsStockable(Boolean.TRUE).setProductStockQuantityMovementCollectionInitialValueFromObject(10));
    	
    	String cashRegisterMovementCollectionCode = testCase.getRandomAlphabetic();
    	testCase.create(inject(MovementCollectionBusiness.class).instanciateOne(cashRegisterMovementCollectionCode).setValue(new BigDecimal("0")));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = testCase.getRandomAlphabetic();
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,salableProductCode01));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("10"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableProduct(salableProductCode01, "0");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "1000", "0");
    	
    	IdentifiableCollection identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	Movement movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("200"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "800", "1");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        	);
    	
    	//2nd pay
    	identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("500"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "300", "2");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        	);
    	
    	//3rd pay
    	identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("300"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "0", "3");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        		, new String[]{"2","-300","0","false"}
        	);
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "0", "1");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        		, new String[]{"2","-300","0","false"}
        	);
    	testCase.assertMovementCollection(cashRegisterMovementCollectionCode, "1000", "3");
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.deleteByCode(Sale.class, saleCode);

    	testCase.assertNullByBusinessIdentifier(MovementCollection.class, RootConstant.Code.generate(saleCode,SALE_BALANCE));
    	testCase.assertMovementCollection(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER), "10", "2");
    	testCase.assertMovements(RootConstant.Code.generate(salableProductCode01,STOCK_REGISTER)
        		, new String[]{"0","-10","0","false"}
        		, new String[]{"1","10","10","true"}
        	);
    	testCase.assertMovementCollection(cashRegisterMovementCollectionCode, "1000", "3");
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class,StockableProduct.class);
    	
    	
    }
    
    /* Exceptions */
    
	@Test
    public void throwProductIsNull(){
		TestCase testCase = instanciateTestCase();
		testCase.assertThrowable(new Runnable(testCase) {
			private static final long serialVersionUID = 1L;
			@Override protected void __run__() throws Throwable {create(instanciateOne(SalableProduct.class).setCode(testCase.getRandomAlphabetic()));}
    	}, FieldHelper.Field.get(SalableProduct.class,SalableProduct.FIELD_PRODUCT).getIdentifier(ConditionHelper.Condition.Builder.Null.class)
				, "La valeur de l'attribut <<produit>> de l'entité <<produit vendable>> doit être non nulle.");
    	testCase.clean();
	}
    
	@Test
    public void throwCollectionIsNull(){
		TestCase testCase = instanciateTestCase();
		testCase.assertThrowable(new Runnable(testCase) {
			private static final long serialVersionUID = 1L;
			@Override protected void __run__() throws Throwable {create(instanciateOne(SalableProductCollectionItem.class));}
    	}, FieldHelper.Field.get(SalableProductCollectionItem.class,SalableProductCollectionItem.FIELD_COLLECTION).getIdentifier(ConditionHelper.Condition.Builder.Null.class)
				, "La valeur de l'attribut <<collection>> de l'entité <<élément de collection de produit vendable>> doit être non nulle.");
    	testCase.clean();
	}
	
    /**/
    
    
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Sale.class,Movement.class,Party.class,Store.class,StockableProduct.class);
		}
		
    }
}
