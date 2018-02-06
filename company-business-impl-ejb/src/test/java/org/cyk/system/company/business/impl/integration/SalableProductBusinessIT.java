package org.cyk.system.company.business.impl.integration;

import java.util.Arrays;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.FieldHelper;
import org.junit.Test;

public class SalableProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override 
    protected void populate() {
    	create(new TangibleProduct().setCode("TP001").setName("TP 001"));
    	create(new TangibleProduct().setCode("TP002").setName("TP 002"));
    }
        
    @Test
    public void crudSalableProductBasedOnExistingProductByJoin(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setProduct(inject(ProductDao.class).readByGlobalIdentifierCode("TP001"));
    	
    	testCase.create(salableProduct);
    	testCase.assertFieldValueEquals(SalableProduct.class, "TP001"
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setCode("TP001");
    	salableProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	salableProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT));
    	salableProduct.setProductClass(TangibleProduct.class);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, "TP001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setCode("TP003");
    	salableProduct.setCascadeOperationToMaster(Boolean.TRUE);
    	salableProduct.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT));
    	salableProduct.setProductClass(TangibleProduct.class);
    	
    	testCase.create(salableProduct);
    	testCase.read(SalableProduct.class, "TP003");
    	testCase.clean();
    }
}
