package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.impl.FakedDataSet;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper.TestCase;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;



public class SaleBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;

    @Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	salableProductCollection.setCode(RandomHelper.getInstance().getAlphabetic(3));
    	salableProductCollection.getCost().setNumberOfProceedElements(new BigDecimal(0));
    	salableProductCollection.getCost().setTurnover(new BigDecimal(0));
    	salableProductCollection.getCost().setValue(new BigDecimal(0));
    	salableProductCollection.getCost().setTax(new BigDecimal(0));
    	testCase.create(salableProductCollection);
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItems(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne();
    	String salableProductCollectionCode = RandomHelper.getInstance().getAlphabetic(3);
    	salableProductCollection.setCode(salableProductCollectionCode);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection);
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	inject(SalableProductCollectionItemBusiness.class).computeChanges(salableProductCollectionItem);
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(salableProductCollection);
    	assertEquals(1, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	salableProductCollection = testCase.read(SalableProductCollection.class, salableProductCollectionCode);
    	salableProductCollection.getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(salableProductCollection);
    	assertEquals(1, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	testCase.clean();
    }
}
