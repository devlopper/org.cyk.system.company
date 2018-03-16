package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.business.impl.__test__.Runnable;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.PartyBusinessRole;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.ConditionHelper;
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
    	String tangibleProductCode = testCase.getRandomHelper().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setProductFromCode(tangibleProductCode));
    	
    	testCase.assertFieldValueEquals(SalableProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String tangibleProductCode = testCase.getRandomHelper().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,tangibleProductCode).setName("TP 001"));
    	
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(tangibleProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class));
    	
    	testCase.assertFieldValueEquals(SalableProduct.class, tangibleProductCode
    			, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME),"TP 001");
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductBasedOnNonExistingProductByCode(){
    	TestCase testCase = instanciateTestCase(); 
    	String salableProductCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class));
    	
    	testCase.assertNotNull(TangibleProduct.class, salableProductCode);
    	testCase.assertNotNull(SalableProduct.class, salableProductCode);
    	testCase.clean();
    }
    
    @Test
    public void crudSalableProductByCodeAndJoinProviderAndStock(){
    	TestCase testCase = instanciateTestCase(); 
    	String productProviderCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(Company.class,productProviderCode));
    	
    	String salableProductCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(SalableProduct.class).setCode(salableProductCode).setCascadeOperationToMaster(Boolean.TRUE)
    			.setCascadeOperationToMasterFieldNames(Arrays.asList(SalableProduct.FIELD_PRODUCT)).setProductClass(TangibleProduct.class)
    			.setIsProductStockable(Boolean.TRUE).setProductProviderPartyFromCode(productProviderCode)
    			);
    	
    	testCase.assertNotNull(TangibleProduct.class, salableProductCode);
    	testCase.assertNotNull(SalableProduct.class, salableProductCode);
    	testCase.assertNotNull(StockableTangibleProduct.class, salableProductCode);
    	assertNotNull(inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByIdentifiableGlobalIdentifierByRole(testCase.read(Company.class, productProviderCode)
    			, testCase.read(TangibleProduct.class, salableProductCode).getGlobalIdentifier(),testCase.read(PartyBusinessRole.class
    					, RootConstant.Code.PartyBusinessRole.PROVIDER)));
    	
    	testCase.clean();
    }
    
    /* Exceptions */
    
	@Test
    public void throwProductIsNull(){
		TestCase testCase = instanciateTestCase();
		testCase.assertThrowable(new Runnable(testCase) {
			private static final long serialVersionUID = 1L;
			@Override protected void __run__() throws Throwable {create(instanciateOne(SalableProduct.class).setCode(RandomHelper.getInstance().getAlphabetic(5)));}
    	}, FieldHelper.Field.get(SalableProduct.class,SalableProduct.FIELD_PRODUCT).getIdentifier(ConditionHelper.Condition.Builder.Null.class)
				, "La valeur de l'attribut <<produit>> de l'entité <<produit vendable>> doit être non nulle.");
    	testCase.clean();
	}
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(SalableProduct.class,Movement.class,Party.class);
		}
		
    }
}
