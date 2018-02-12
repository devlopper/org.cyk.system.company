package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.impl__data__.DataSet;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class SaleBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;

    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	testCase.create(sale);
    	testCase.assertSaleCost(saleCode,null,null,null,null);
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 0l);
    	testCase.assertSalableProductCollection(saleCode,"0","0","0","0");
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItem(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 1l);
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 1l);
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 2l);
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 1l);
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, 2l);
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    }
    
    @Test
    public void computeChanges(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "0", "0", "0", "0");
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "2","200","31","169");
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(),"3","350","54","296");
    	
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Sale.class,Movement.class);
		}
		
    }
}
