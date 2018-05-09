package org.cyk.system.company.business.impl.__test__;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.utility.common.helper.FieldHelper;

public class TestCase extends org.cyk.system.root.business.impl.__test__.TestCase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public void assertStockableProduct(StockableProduct stockableProduct,String expectedQuantity){
		//assertBigDecimalEquals("stockable tangible product quantity is not equal", expectedQuantity, stockableProduct.getQuantityMovementCollectionInitialValue());
    }
	
	public void assertStockableProductStore(StockableProductStore stockableProductStore,String expectedQuantity){
		assertEqualsNumber("stockable product store quantity is not equal", expectedQuantity, stockableProductStore.getQuantityMovementCollection().getValue());
    }
	
	public void assertStockableProduct(String code,String expectedQuantity){
		StockableProduct stockableProduct = read(StockableProduct.class, code);
		assertStockableProduct(stockableProduct, expectedQuantity);
	}
	
	public void assertStockableProductStore(String code,String expectedQuantity){
		StockableProductStore stockableProductStore = read(StockableProductStore.class, code);
		inject(StockableProductStoreBusiness.class).setQuantityMovementCollection(stockableProductStore);
		assertStockableProductStore(stockableProductStore, expectedQuantity);
	}
	
	public TestCase assertEqualsCost(Cost expected,Cost actual){
		assertEqualsByFieldValue(expected, actual, Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS);
		assertEqualsByFieldValue(expected, actual, Cost.FIELD_VALUE);
		assertEqualsByFieldValue(expected, actual, Cost.FIELD_TAX);
		assertEqualsByFieldValue(expected, actual, Cost.FIELD_TURNOVER);
		return this;
	}
	
	public TestCase assertEqualsSalableProductStoreCollectionCost(Cost expected,String identifier){
		assertEqualsCost(expected, read(SalableProductStoreCollection.class, identifier).getCost());
		return this;
	}
	
	public void assertCost(Cost cost,Object expectedNumberOfElements,Object expectedValue,Object expectedTax,Object expectedTurnover){
    	assertEqualsFieldValues(cost, new FieldHelper.Field.Value.Collection().addValue(Cost.class, expectedNumberOfElements, Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS)
    			.addValue(Cost.class, expectedValue, Cost.FIELD_VALUE)
    			.addValue(Cost.class, expectedTax, Cost.FIELD_TAX)
    			.addValue(Cost.class, expectedTurnover, Cost.FIELD_TURNOVER));
    }
	
	public void assertSalableProductCollectionCost(SalableProductCollection salableProductCollection,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertCost(salableProductCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductCollectionCost(String code,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertSalableProductCollectionCost(inject(SalableProductCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
	
	public void assertSalableProductCollection(SalableProductCollection salableProductCollection,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertCost(salableProductCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductCollection(String code,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertSalableProductCollection(inject(SalableProductCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductStoreCollectionCost(SalableProductStoreCollection salableProductStoreCollection,Object expectedCostNumberOfElements,Object expectedCostValue
    		,Object expectedCostTax,Object expectedCostTurnover){
    	assertCost(salableProductStoreCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductStoreCollectionCost(String code,Object expectedCostNumberOfElements,Object expectedCostValue
    		,Object expectedCostTax,Object expectedCostTurnover){
    	assertSalableProductStoreCollectionCost(inject(SalableProductStoreCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSaleCost(String code,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertSalableProductStoreCollectionCost(inject(SaleDao.class).read(code).getSalableProductStoreCollection(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
	
    public TestCase assertNotNullStockableProductStoreReadByProductStore(String productCode,String storeCode){
    	StockableProductStore stockableProductStore = inject(StockableProductStoreDao.class).readByProductStore(inject(ProductStoreDao.class)
    			.readByProductCodeByStoreCode(productCode, storeCode));
    	assertNotNull(stockableProductStore);
    	assertEquals(productCode, stockableProductStore.getProductStore().getProduct().getCode());
    	assertEquals(storeCode, stockableProductStore.getProductStore().getStore().getCode());
    	return this;
    }
    
    public TestCase assertNullStockableProductStoreReadByProductStore(String productCode,String storeCode){
    	StockableProductStore stockableProductStore = inject(StockableProductStoreDao.class).readByProductStore(inject(ProductStoreDao.class)
    			.readByProductCodeByStoreCode(productCode, storeCode));
    	assertNull(stockableProductStore);
    	return this;
    }
    
    public TestCase assertEqualsStockableProductStoresByStoreCode(String storeCode,Collection<String> expectedProductCodes){
    	Collection<String> codes = new ArrayList<>();
    	for(StockableProductStore index : inject(StockableProductStoreDao.class).readByStoreCodes(storeCode))
    		codes.add(index.getProductStore().getProduct().getCode());
    	assertCollection(expectedProductCodes, codes);
    	return this;
    }
    
    public TestCase assertEqualsStockableProductStoresByStoreCode(String storeCode,String...expectedProductCodes){
    	assertEqualsStockableProductStoresByStoreCode(storeCode,Arrays.asList(expectedProductCodes));
    	return this;
    }
}