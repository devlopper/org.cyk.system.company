package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.business.impl.__test__.Runnable;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.ConditionHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class SalableProductCollectionBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
   
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudSalableProductCollection(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	testCase.create(testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode));
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "0");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,null,null,null,null);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	testCase.create(testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode).setItemsSynchonizationEnabled(Boolean.TRUE));
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "0");    	
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"0","0","0","0");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithOneItem(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
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
    	String salableProductCode01 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")));
    	
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	SalableProductCollection salableProductCollection = testCase.instanciateOne(SalableProductCollection.class, salableProductCollectionCode)
    			.setItemsSynchonizationEnabled(Boolean.TRUE);
    	
    	salableProductCollection.add(testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode01)
    			.setCostNumberOfProceedElements(2),testCase.instanciateOne(SalableProductCollectionItem.class).setSalableProductFromCode(salableProductCode02)
    			.setCostNumberOfProceedElements(3));
    	
    	testCase.create(salableProductCollection);
    	
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "2");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"5","650","100","550");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("150")));
    	
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
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
    	
    	testCase.clean();
    }
    
    @Test
    public void computeChanges(){
    	TestCase testCase = instanciateTestCase();
    	String salableProductCode01 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode01).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setPrice(new BigDecimal("100")));
    	String salableProductCode02 = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode02).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
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
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemWithProductQuantityUpdatable(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("2"));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	testCase.assertStockableTangibleProduct(FakedDataSet.TANGIBLE_PRODUCT_TP1, "8");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode);
    	salableProductCollection.getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, "1");
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	testCase.assertStockableTangibleProduct(FakedDataSet.TANGIBLE_PRODUCT_TP1, "8");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemWithProductQuantityUpdatableExceedUpperLimit(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne().setIsStockMovementCollectionUpdatable(Boolean.TRUE);
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.getCost().setNumberOfProceedElements(new BigDecimal("11"));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	
    	testCase.clean();
    }
    
    /* Exceptions */
    
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
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(SalableProductCollection.class,Movement.class);
		}
		
    }
}
