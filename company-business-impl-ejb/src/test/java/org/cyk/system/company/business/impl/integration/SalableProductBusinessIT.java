package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.impl__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class SalableProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
        
    @Test
    public void crudSalableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setProduct(inject(ProductDao.class).readByGlobalIdentifierCode(FakedDataSet.TANGIBLE_PRODUCT_NO_SALABLE_TP1));
    	
    	testCase.create(salableProduct);
    	testCase.assertFieldValueEquals(SalableProduct.class, FakedDataSet.TANGIBLE_PRODUCT_NO_SALABLE_TP1
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setCode(FakedDataSet.TANGIBLE_PRODUCT_NO_SALABLE_TP1);
    	salableProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	salableProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT));
    	salableProduct.setProductClass(TangibleProduct.class);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, FakedDataSet.TANGIBLE_PRODUCT_NO_SALABLE_TP1);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	String code = RandomHelper.getInstance().getAlphabetic(5);
    	salableProduct.setCode(code);
    	salableProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	salableProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT));
    	salableProduct.setProductClass(TangibleProduct.class);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, code);
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(SalableProduct.class);
		}
		
    }
}
