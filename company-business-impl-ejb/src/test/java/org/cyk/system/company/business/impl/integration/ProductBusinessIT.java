package org.cyk.system.company.business.impl.integration;

import static org.cyk.system.root.model.RootConstant.Code.BusinessRole.PROVIDER;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FileHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class ProductBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
        
    @Test
    public void crudOneTangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	TangibleProduct tangibleProduct = testCase.instanciateOne(TangibleProduct.class,code);
    	testCase.create(tangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,code);
    	testCase.clean();
    }
    
    @Test
    public void crudOneTangibleProductAndImage(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	TangibleProduct tangibleProduct = testCase.instanciateOne(TangibleProduct.class,code);
    	FileHelper.File file = RandomHelper.getInstance().getFilePersonHeadOnly(Boolean.TRUE);
    	tangibleProduct.setImage(inject(FileBusiness.class).process(file.getBytes(), file.getName()));
    	testCase.create(tangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,code);
    	testCase.clean();
    }
    
    @Test
    public void crudOneIntangibleProduct(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	IntangibleProduct intangibleProduct = testCase.instanciateOne(IntangibleProduct.class,code);
    	testCase.create(intangibleProduct);
    	testCase.assertNull(StockableTangibleProduct.class,code);
    	testCase.clean();
    }
    
    @Test
    public void crudOneProductCategory(){
    	TestCase testCase = instanciateTestCase(); 
    	String code = testCase.getRandomHelper().getAlphabetic(5);
    	ProductCategory productCategory = testCase.instanciateOne(ProductCategory.class,code);
    	testCase.create(productCategory);
    	testCase.clean();
    }
    
    @Test
    public void crudProductAndJoinProviderAndStock(){
    	TestCase testCase = instanciateTestCase(); 
    	String productProviderCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(Company.class,productProviderCode));
    	
    	String productCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(testCase.instanciateOne(TangibleProduct.class,productCode).setIsStockable(Boolean.TRUE).setProviderPartyFromCode(productProviderCode));
    	
    	testCase.assertNotNull(StockableTangibleProduct.class, productCode);
    	assertNotNull(inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByIdentifiableGlobalIdentifierByRole(testCase.read(Company.class, productProviderCode)
    			, testCase.read(TangibleProduct.class, productCode).getGlobalIdentifier(),testCase.read(BusinessRole.class, PROVIDER)));
    	
    	testCase.clean();
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Product.class,Movement.class,Party.class);
		}
		
    }
}
