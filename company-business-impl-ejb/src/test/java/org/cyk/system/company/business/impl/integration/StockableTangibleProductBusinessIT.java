package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class StockableTangibleProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudStockableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	StockableTangibleProduct stockableTangibleProduct = inject(StockableTangibleProductBusiness.class).instanciateOne();
    	stockableTangibleProduct.setTangibleProduct(inject(TangibleProductDao.class).read(FakedDataSet.TANGIBLE_PRODUCT_NO_STOCKABLE_TP1));
    	
    	testCase.create(stockableTangibleProduct);
    	testCase.assertFieldValueEquals(StockableTangibleProduct.class, FakedDataSet.TANGIBLE_PRODUCT_NO_STOCKABLE_TP1
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudStockableTangibleProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	StockableTangibleProduct stockableTangibleProduct = inject(StockableTangibleProductBusiness.class).instanciateOne();
    	stockableTangibleProduct.setCode(FakedDataSet.TANGIBLE_PRODUCT_NO_STOCKABLE_TP1);
    	stockableTangibleProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	stockableTangibleProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT));
    	
    	testCase.create(stockableTangibleProduct);
    	testCase.read(StockableTangibleProduct.class, FakedDataSet.TANGIBLE_PRODUCT_NO_STOCKABLE_TP1);
    	testCase.clean();
    }
    
    @Test
    public void crudStockableTangibleProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	StockableTangibleProduct stockableTangibleProduct = inject(StockableTangibleProductBusiness.class).instanciateOne();
    	String code = RandomHelper.getInstance().getAlphabetic(5);
    	stockableTangibleProduct.setCode(code);
    	stockableTangibleProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	stockableTangibleProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(StockableTangibleProduct.FIELD_TANGIBLE_PRODUCT));
    	
    	testCase.create(stockableTangibleProduct);
    	testCase.read(StockableTangibleProduct.class, code);
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
