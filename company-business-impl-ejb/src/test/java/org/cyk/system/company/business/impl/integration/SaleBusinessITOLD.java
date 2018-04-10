package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.iesa.IesaFakedDataProducer;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.junit.Test;
@Deprecated
public class SaleBusinessITOLD extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    //@Test
    public void crudSalableProductCollection(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{}));
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	testCase.clean();
    }
    
    //@Test
    public void crudSalableProductCollectionItem(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{}));
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne("SPC001", new Object[]{"TP01",1});
    	companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCost(), "1", "60000", "0", "60000");
    	companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCollection().getCost(), "1", "60000", "0", "60000");
    	testCase.create(salableProductCollectionItem);
    	salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "1", "60000", "0", "60000");
    	testCase.clean();
    }
    
    //@Test
    public void crudSalableProductCollectionAndItems(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{}));
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	assertEquals(0, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP01","1","0","0");
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.FALSE);
    	companyBusinessTestHelper.update(salableProductCollection);
    	salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	assertEquals(0, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	salableProductCollection.getItems().getElements().clear();
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP01","1","0","0");
    	companyBusinessTestHelper.update(salableProductCollection);
    	salableProductCollection.getItems().getElements().clear();
    	salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	assertEquals(1, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	testCase.clean();
    }
    
    //@Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollection(){
    	TestCase testCase = instanciateTestCase();
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P001",null, CompanyConstant.Code.CashRegister.DEFAULT, new String[][]{}));
    	testCase.clean();
    }
    
    //@Test
    public void crudSaleCashRegisterMovement(){
    	TestCase testCase = instanciateTestCase();
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P001",null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	SaleCashRegisterMovement saleCashRegisterMovement = companyBusinessTestHelper.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne("P001", "Sale001", "0"));
    	assertEquals("P001_Sale001", saleCashRegisterMovement.getCode());
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne("P001", "Sale002", "0"));
    	
    	testCase.clean();
    }
    
    //@Test
    public void crudSaleCashRegisterMovementCollectionUserInterface(){
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	String sale1Code = "Sale"+RandomDataProvider.getInstance().randomInt(0, 100000);
    	TestCase testCase = instanciateTestCase();
    	//testCase.create(inject(SaleBusiness.class).instanciateOne(sale1Code,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1},{"TP02",2} }));
    	companyBusinessTestHelper.assertSale(sale1Code, "74000", "74000");
    	//companyBusinessTestHelper.assertCost(inject(SaleDao.class).read(saleCode).getSalableProductCollection().getCost(), "3", "74000", "0", "74000");
    	
    	//testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"IP01",4},{"IP02",3} }));
    	companyBusinessTestHelper.assertSale("Sale002", "4610000", "4610000");
    	
    	String pay1Code = "Pay"+RandomDataProvider.getInstance().randomInt(0, 100000);
    	SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(userAccount);
    	saleCashRegisterMovementCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	saleCashRegisterMovementCollection.setCode(pay1Code);
    	
    	inject(SaleCashRegisterMovementCollectionBusiness.class).setCashRegister(userAccount, saleCashRegisterMovementCollection, inject(CashRegisterDao.class)
    			.read(IesaFakedDataProducer.CASH_REGISTER_001));
    	
    	inject(SaleCashRegisterMovementBusiness.class).instanciateOne(saleCashRegisterMovementCollection, sale1Code, "500");
    	
    	SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(saleCashRegisterMovementCollection, "Sale002", "800");
    	
    	assertEquals(2, saleCashRegisterMovementCollection.getItems().getElements().size());
    	
    	testCase.create(saleCashRegisterMovementCollection);
    	
    	assertEquals(pay1Code+"_Sale002", saleCashRegisterMovement.getCode());
    	
    	assertEquals(2, inject(SaleCashRegisterMovementDao.class).readByCollection(saleCashRegisterMovementCollection).size());
    	//companyBusinessTestHelper.assertSaleCashRegisterMovementCollection(pay1Code, "1300", "1300", null, null, null, "1300");
    	
    	//companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(saleCashRegisterMovementCollection)
    	//		.iterator().next().getFile());
    	
    	//companyBusinessTestHelper.createReportFile(saleCashRegisterMovementCollection, CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4
    	//		, Locale.ENGLISH,1);
    	
    	testCase.clean();
    }

    

}
