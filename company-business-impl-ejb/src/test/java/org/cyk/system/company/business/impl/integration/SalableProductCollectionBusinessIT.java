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
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.impl__data__.DataSet;
import org.cyk.utility.common.helper.ClassHelper;
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
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 0l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,null,null,null,null);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 0l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"0","0","0","0");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItem(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 1l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode);
    	salableProductCollection.getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 1l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 2l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"3","350","54","296");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductCollectionWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	testCase.create(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 1l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"2","200","31","169");
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode);
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	salableProductCollection.getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	testCase.update(salableProductCollection);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, salableProductCollectionCode, 2l);
    	testCase.assertSalableProductCollection(salableProductCollectionCode,"3","350","54","296");
    	
    	testCase.clean();
    }
    
    @Test
    public void computeChanges(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	
    	inject(SalableProductCollectionBusiness.class).computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	inject(SalableProductCollectionBusiness.class).computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(), "2","200","31","169");
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	inject(SalableProductCollectionBusiness.class).computeChanges(salableProductCollection);
    	testCase.assertCost(salableProductCollection.getCost(),"3","350","54","296");
    	
    	testCase.clean();
    }
    
    /*
    @Override
    protected void businesses() {
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne("SPC01");
    	SalableProductCollectionItem item1 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP3", "1", "0", "0");
    	companyBusinessTestHelper.assertCost(item1.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	SalableProductCollectionItem item2 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP1", "2", "0", "0");
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "200"));
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	create(salableProductCollection);
    	companyBusinessTestHelper.assertCost((salableProductCollection = inject(SalableProductCollectionDao.class).read(salableProductCollection.getIdentifier()))
    			.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	
    	item1 = inject(SalableProductCollectionItemBusiness.class).find(item1.getCode());
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getCode());
    	
    	item2.getCost().setValue(BigDecimal.ONE);
    	update(item2);
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getIdentifier());
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "1"));
    	
    	inject(SalableProductCollectionItemBusiness.class).computeCost(item2);
    	update(item2);
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getIdentifier());
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "200"));
    	
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	inject(SalableProductCollectionBusiness.class).computeCost(salableProductCollection);
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	
    	inject(SalableProductCollectionBusiness.class).remove(salableProductCollection, item2);
    	update(salableProductCollection);
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	inject(SalableProductCollectionBusiness.class).computeCost(salableProductCollection);
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	
    	SalableProductCollectionItem item3 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP2", "3", "0", "0");
    	item3.setCascadeOperationToMaster(Boolean.TRUE);
    	create(item3);
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "175"));
    }
    */
    /* Exceptions */
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(SalableProductCollection.class);
		}
		
    }
}
