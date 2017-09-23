package org.cyk.system.company.business.impl.iesa;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.business.impl.__data__.IesaFakedDataSet;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementCollectionDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper.TestCase;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.file.FileIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.junit.Test;

public class IesaSaleBusinessIT extends AbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void populate() {
    	super.populate();
    	//create(inject(CustomerBusiness.class).instanciateOneRandomly(AbstractCompanyFakedDataProducer.CUSTOMER_001));
    	//create(inject(CustomerBusiness.class).instanciateOneRandomly(AbstractCompanyFakedDataProducer.CUSTOMER_002));
    	new IesaFakedDataSet().instanciate().save();
    }
    
    @Test
    public void crudSalableProductCollection(){
    	TestCase testCase = instanciateTestCase();
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{});
    	salableProductCollection.setItemAggregationApplied(Boolean.FALSE);
    	testCase.create(salableProductCollection);
    	salableProductCollection = testCase.read(SalableProductCollection.class, "SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	
    	salableProductCollection.getCost().setValueFromString("100").setTaxFromString("3").setTurnoverFromString("97").setNumberOfProceedElementsFromString("2");
    	testCase.update(salableProductCollection);
    	salableProductCollection = testCase.read(SalableProductCollection.class, "SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "2", "100", "3", "97");
    	
    	testCase.clean();
    	
    }
    
    @Test
    public void crudSalableProductCollectionItem(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{}));
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne("SPC001", new Object[]{"TP01",1});
    	companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCost(), "1", "60000", "0", "60000");
    	companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCollection().getCost(), "1", "60000", "0", "60000");
    	testCase.create(salableProductCollectionItem);
    	salableProductCollection = testCase.read(SalableProductCollection.class,"SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "1", "60000", "0", "60000");
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne("SPC001", new Object[]{"TP02",1});
    	companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCost(), "1", "7000", "0", "7000");
    	//companyBusinessTestHelper.assertCost(salableProductCollectionItem.getCollection().getCost(), "2", "7000", "0", "7000");
    	testCase.create(salableProductCollectionItem);
    	salableProductCollection = testCase.read(SalableProductCollection.class,"SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "2", "67000", "0", "67000");
    	
    	testCase.clean();
    }
    
    @Test
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
    
    @Test
    public void crudSale(){
    	
    	/*TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	
    	companyBusinessTestHelper.assertCost(inject(SaleDao.class).read("Sale001").getSalableProductCollection().getCost(), "0", "0", "0", "0");
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne("Sale001", new Object[]{"TP01",1});
    	testCase.create(salableProductCollectionItem);
    	companyBusinessTestHelper.assertCost(inject(SaleDao.class).read("Sale001").getSalableProductCollection().getCost(), "1", "60000", "0", "60000");
    	testCase.clean();
    	*/
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollection(){
    	TestCase testCase = instanciateTestCase();
    	String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	String sale002 = RandomStringUtils.randomAlphanumeric(20);
    	String p001 = RandomStringUtils.randomAlphanumeric(20);
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale002,IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovement(){
    	String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	String sale002 = RandomStringUtils.randomAlphanumeric(20);
    	String p001 = RandomStringUtils.randomAlphanumeric(20);
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1} }));
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale002,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1} }));
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	SaleCashRegisterMovement saleCashRegisterMovement = testCase.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p001, sale001, "1"));
    	assertEquals(p001+"_"+sale001, saleCashRegisterMovement.getCode());
    	
    	testCase.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p001, sale002, "1"));
    	
    	testCase.clean();
    }
    
    @Test
    public void assertOrderSaleCashRegisterMovement(){
    	String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	String sale002 = RandomStringUtils.randomAlphanumeric(20);
    	String p001 = RandomStringUtils.randomAlphanumeric(20);
    	String p002 = RandomStringUtils.randomAlphanumeric(20);
    	String p003 = RandomStringUtils.randomAlphanumeric(20);
    	String p004 = RandomStringUtils.randomAlphanumeric(20);
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1} }));
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale002,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1} }));
    	
    	SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{});
    	testCase.create(saleCashRegisterMovementCollection);
    	
    	SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p001, sale001, "1");
    	saleCashRegisterMovement.getGlobalIdentifier().getExistencePeriod().setFromDate(date(2000, 1, 5));
    	testCase.create(saleCashRegisterMovement);
    	String code001 = saleCashRegisterMovement.getCode();
    	
    	testCase.assertOrderBasedOnExistencePeriodFromDate(SaleCashRegisterMovement.class, code001);
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p002,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p002, sale001, "1");
    	saleCashRegisterMovement.setBirthDate(date(2000, 1, 3));
    	testCase.create(saleCashRegisterMovement);
    	String code002 = saleCashRegisterMovement.getCode();
    	
    	testCase.assertOrderBasedOnExistencePeriodFromDate(SaleCashRegisterMovement.class,code002,code001);
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p003,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p003, sale001, "1");
    	saleCashRegisterMovement.setBirthDate(date(2000, 1, 4));
    	testCase.create(saleCashRegisterMovement);
    	String code003 = saleCashRegisterMovement.getCode();
    	testCase.assertOrderBasedOnExistencePeriodFromDate(SaleCashRegisterMovement.class,code002,code003,code001);
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p004,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p004, sale001, "1");
    	saleCashRegisterMovement.setBirthDate(date(2000, 1, 1));
    	testCase.create(saleCashRegisterMovement);
    	String code004 = saleCashRegisterMovement.getCode();
    	
    	testCase.assertOrderBasedOnExistencePeriodFromDate(SaleCashRegisterMovement.class, code004 ,code002, code003,code001);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollectionUserInterface(){
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	String sale1Code = "Sale"+RandomDataProvider.getInstance().randomInt(0, 100000);
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale1Code,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1},{"TP02",2} }));
    	companyBusinessTestHelper.assertSale(sale1Code, "74000", "74000");
    	//companyBusinessTestHelper.assertCost(inject(SaleDao.class).read(saleCode).getSalableProductCollection().getCost(), "3", "74000", "0", "74000");
    	
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"IP01",4},{"IP02",3} }));
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
    	companyBusinessTestHelper.assertSaleCashRegisterMovementCollection(pay1Code, "1300", "1300", null, null, null, "1300");
    	
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(saleCashRegisterMovementCollection)
    			.iterator().next().getFile());
    	
    	companyBusinessTestHelper.createReportFile(saleCashRegisterMovementCollection, CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4
    			, Locale.ENGLISH,1);
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollectionAndVariousCashRegisterMovementMode(){
    	TestCase testCase = instanciateTestCase();
    	String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	String sale002 = RandomStringUtils.randomAlphanumeric(20);
    	String p001 = RandomStringUtils.randomAlphanumeric(20);
    	String p002 = RandomStringUtils.randomAlphanumeric(20);
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new String[][]{{"TP01","3"},{"TP02","2"}}));
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale002,IesaFakedDataProducer.CUSTOMER_001, new String[][]{{"TP03","4"},{"TP04","5"}}));
    	
    	SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001
    			,CompanyConstant.Code.CashRegisterMovementMode.CASH, new String[][]{ {sale001, "100"},{sale002, "250"} });
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString("Yeo Gérard");
    	testCase.create(saleCashRegisterMovementCollection);
    	assertThat("Stamp duty",inject(SaleCashRegisterMovementCollectionDao.class).read(p001).getCashRegisterMovement().getStampDutyInterval()!=null);
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(inject(SaleCashRegisterMovementCollectionDao.class).read(p001))
    			.iterator().next().getFile());
    	
    	saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p002,null, IesaFakedDataProducer.CASH_REGISTER_001
    			,CompanyConstant.Code.CashRegisterMovementMode.CHEQUE, new String[][]{ {sale001, "300"},{sale002, "500"} });
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString("Issia Koné");
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setGenerator("BICICI");
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setCode(RandomStringUtils.randomAlphanumeric(10).toUpperCase());
    	testCase.create(saleCashRegisterMovementCollection);
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(inject(SaleCashRegisterMovementCollectionDao.class).read(p002))
    			.iterator().next().getFile());
    	
    	saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P003",null, IesaFakedDataProducer.CASH_REGISTER_001
    			,CompanyConstant.Code.CashRegisterMovementMode.BANK_TRANSFER, new String[][]{ {sale001, "300"},{sale002, "500"} });
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString("Koukou mélé");
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setCode(RandomStringUtils.randomAlphanumeric(10).toUpperCase());
    	testCase.create(saleCashRegisterMovementCollection);
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(inject(SaleCashRegisterMovementCollectionDao.class).read("P003"))
    			.iterator().next().getFile());
    	
    	saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P004",null, IesaFakedDataProducer.CASH_REGISTER_001
    			,CompanyConstant.Code.CashRegisterMovementMode.MOBILE_PAYMENT, new String[][]{ {sale001, "300"},{sale002, "500"} });
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString("Léon paul");
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setCode(RandomStringUtils.randomAlphanumeric(10).toUpperCase());
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setGenerator("Orange");
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getSupportingDocument().setContentWriter("22551037");
    	testCase.create(saleCashRegisterMovementCollection);
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(inject(SaleCashRegisterMovementCollectionDao.class).read("P004"))
    			.iterator().next().getFile());
    	
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale003",IesaFakedDataProducer.CUSTOMER_002, new String[][]{{"TP01","3"},{"TP02","2"}}));
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale004",IesaFakedDataProducer.CUSTOMER_002, new String[][]{{"TP03","4"},{"TP04","5"}}));
    	
    	saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("PA01",null, IesaFakedDataProducer.CASH_REGISTER_001
    			,CompanyConstant.Code.CashRegisterMovementMode.CASH, new String[][]{ {"Sale003", "100"},{"Sale004", "250"} });
    	saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString("Yeo Gérard");
    	testCase.create(saleCashRegisterMovementCollection);
    	companyBusinessTestHelper.write(inject(FileIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifier(inject(SaleCashRegisterMovementCollectionDao.class).read("PA01"))
    			.iterator().next().getFile());
    	
    	testCase.clean();
    }
    
    @Test
    public void regularCase(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1},{"TP02",2} }));
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"IP01",4},{"IP02",3} }));
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P001",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{"Sale001","500"},{"Sale002","800"}}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P002",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{"Sale001","700"},{"Sale002","300"}}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P003",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{"Sale002","100"},{"Sale001","250"}}));
    	
    	testCase.deleteByCode(Sale.class, "Sale001");
    	
    	testCase.clean();
    }
    
    @Test
    public void updateSaleCashRegisterMovement(){
    	TestCase testCase = instanciateTestCase();
    	String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	String sale002 = RandomStringUtils.randomAlphanumeric(20);
    	String p001 = RandomStringUtils.randomAlphanumeric(20);
    	String p002 = RandomStringUtils.randomAlphanumeric(20);
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1},{"TP02",2} }));
    	companyBusinessTestHelper.assertCost(inject(SaleDao.class).read(sale001).getSalableProductCollection().getCost(), "3", "74000", "0", "74000");
    	
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale002,IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"IP01",4},{"IP02",3} }));
    	
    	//create 1st payment
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{sale001,"500"}}));
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p001+"_"+sale001, "73500","74000", "73500", "500");
    	
    	//update 1st payment
    	SaleCashRegisterMovement saleCashRegisterMovement = testCase.read(SaleCashRegisterMovement.class, p001+"_"+sale001);
    	saleCashRegisterMovement.setAmount(new BigDecimal("1000"));
    	testCase.update(saleCashRegisterMovement);
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p001+"_"+sale001, "73000","74000", "73000", "1000");
    	
    	//create 2nd payment
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p002,null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{sale001,"500"}}));
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p002+"_"+sale001, "72500","74000", "72500", "500");
    	
    	//update 2nd payment
    	saleCashRegisterMovement = testCase.read(SaleCashRegisterMovement.class, p002+"_"+sale001);
    	saleCashRegisterMovement.setAmount(new BigDecimal("1000"));
		testCase.update(saleCashRegisterMovement);
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p001+"_"+sale001, "73000","74000", "72000", "1000");
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p002+"_"+sale001, "72000","74000", "72000", "1000");
    	
    	//update 1st payment
    	saleCashRegisterMovement = testCase.read(SaleCashRegisterMovement.class, p001+"_"+sale001);
    	saleCashRegisterMovement.setAmount(new BigDecimal("800"));
    	testCase.update(saleCashRegisterMovement);
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p001+"_"+sale001, "73200","74000", "72200", "800");
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p002+"_"+sale001, "72200","74000", "72200", "1000");
    	
    	//update 1st sale
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale001, new Object[]{"TP03",3});
    	salableProductCollectionItem.setCascadeOperationToChildren(Boolean.TRUE);
    	testCase.create(salableProductCollectionItem);
    	
    	companyBusinessTestHelper.assertCost(inject(SaleDao.class).read(sale001).getSalableProductCollection().getCost(), "6", "95000", "0", "95000");
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p001+"_"+sale001, "94200","95000", "93200", "800");
    	companyBusinessTestHelper.assertSaleCashRegisterMovement(p002+"_"+sale001, "93200","95000", "93200", "1000");
    	
    	//
    	/*
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p002,null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{sale001,"700"},{sale002,"300"}}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P003",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new Object[][]{{sale002,"100"},{sale001,"250"}}));
    	*/
    	testCase.clean();
    }
    
    /* Complex Crud */
    
    /* Exceptions */
    
    @Test
    public void saleBalanceCannotBeLessThanZero(){
    	final String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	final String p001 = RandomStringUtils.randomAlphanumeric(20);
    	final TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	testCase.throwMessage(new Runnable() {
			@Override
			public void run() {
				testCase.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p001, sale001, "1"));
			}
		}, "La balance(-1) doit être supérieure ou égale à 0.");
    	
    	testCase.clean();
    }
    
    @Test
    public void saleCashRegisterMovementAmountCannotBeLessThanZero(){
    	final TestCase testCase = instanciateTestCase();
    	final String sale001 = RandomStringUtils.randomAlphanumeric(20);
    	final String p001 = RandomStringUtils.randomAlphanumeric(20);
    	testCase.create(inject(SaleBusiness.class).instanciateOne(sale001,IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale001, new Object[]{"TP01",1}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(p001,null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	testCase.throwMessage(new Runnable() {
			@Override
			public void run() {
				testCase.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne(p001, sale001, "-1"));
			}
		}, "Le montant(-1) doit être supérieur à 0.");
    	
    	testCase.clean();
    }
    
    //@Test
    public void crudSalableProductCollectionItem1(){
    	/*companyBusinessTestHelper.createSalableProductCollection("SPC002","School Fees",new Cost().setValue(new BigDecimal("0")), new Object[][]{}, "0");
    	SalableProductCollectionItem salableProductCollectionItemTP01 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP01",1}, "100");
    	SalableProductCollectionItem salableProductCollectionItemTP02 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP02",2}, "400");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP01, "300");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",3}, "975");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP02, "675");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",1}, "900");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItem, "675");
    	*/
    }
    
    //@Test
    public void crudSaleOld(){
    	/*sale = companyBusinessTestHelper.createSale(null,CUSTOMER_001, new Object[][]{{"IP01",1},{"IP02",1,100000},{"TP01",1},{"TP02",1},{"TP03",1},{"TP04",1},{"IP03",1},{"TP05",2},{"TP06",1}
    	,{"IP04",3},{"IP05",3},{"IP06",3},{"IP07",10}});
    	*/
    	//SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = companyBusinessTestHelper
    	//		.createSaleCashRegisterMovementCollection("PCrudSale001", CASH_REGISTER_001, new String[][]{});
    	
    	//companyBusinessTestHelper.createReportFile(sale, CompanyConstant.Code.ReportTemplate.INVOICE, Locale.ENGLISH, 0);
    	/*
    	CreateReportFileArguments<Sale> createSaleReportFileArguments = new CreateReportFileArguments<Sale>(sale);
    	createSaleReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.INVOICE));	
    	inject(SaleBusiness.class).createReportFile(createSaleReportFileArguments);
    	
    	String fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.INVOICE;
		FileRepresentationType fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, sale).iterator().next());
    	*/
    	//sale = companyBusinessTestHelper.createSale(null, new Object[][]{}, "0", "0");
    	
    	/*
    	String saleCode = sale.getCode();
    	assertThat("Sale "+saleCode+" exists", inject(SaleBusiness.class).find(saleCode)!=null);
    	assertThat("Salable product collection "+saleCode+" exists", inject(SalableProductCollectionBusiness.class).find(saleCode)!=null);
    	assertThat("Sale code start with FACT", StringUtils.startsWith(saleCode, "FACT"));
    	*/
    	
    	/*SaleCashRegisterMovement saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement("PCrudSale001",saleCode, "15000",new String[][]{
    		{"IP01","15000"}
    	}/*, "2158000","15000");*/
    	/*CreateReportFileArguments<SaleCashRegisterMovement> createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
    	*/
		/*saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement("PCrudSale001",saleCode, "550000",new String[][]{
    		{"IP01","50000"},{"IP02","500000"}
    	}/*, "1608000","565000");*/
    	/*createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
		*/
		/*saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement("PCrudSale001",saleCode, "920000",new String[][]{
    		{"IP02","500000"},{"TP01","60000"},{"IP04","100000"},{"IP05","90000"},{"IP06","90000"},{"IP07","80000"}
    	}/*, "688000","1485000");*/
    	/*createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
		*/
		/*
		DataReadConfiguration dataReadConfiguration = new DataReadConfiguration();
		dataReadConfiguration.setGlobalFilter("x");
		assertEquals(1l, inject(SaleBusiness.class).countAll());
		
		dataReadConfiguration.setGlobalFilter("F");
		
		assertEquals(1l, inject(SaleBusiness.class).countByString((String)null));
		assertEquals(1l, inject(SaleBusiness.class).countByString(""));
		
		assertEquals(1l, inject(SaleBusiness.class).countByString("F"));
		
		assertEquals(0l, inject(SaleBusiness.class).countByString("X"));
		*/
    	//companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "200", "200","350");
    	
    	//companyBusinessTestHelper.deleteSale(sale);
    }
    
    //@Test
    public void crudSaleCashRegisterMovementCollection1(){
    	/*Sale sale1 = companyBusinessTestHelper.createSale("FCrudSaleCashRegisterMovementCollection001",CUSTOMER_001, new Object[][]{{"TP01",1},{"TP02",1,100000},{"IP01",1}});
    	Sale sale2 = companyBusinessTestHelper.createSale("FCrudSaleCashRegisterMovementCollection002",CUSTOMER_001, new Object[][]{{"TP03",1},{"TP04",2}});
    	companyBusinessTestHelper.createSaleCashRegisterMovementCollection("PCrudSaleCashRegisterMovementCollection001", CASH_REGISTER_001
    			, new String[][]{{sale1.getCode(),"1000"},{sale2.getCode(),"500"}});
    	
    	companyBusinessTestHelper.assertSaleCashRegisterMovementCollection("PCrudSaleCashRegisterMovementCollection001", "1500", "1500", null, null, null, "1500");
    	*/
    }
    
    //@Test
    public void crudSaleCashRegisterMovement1(){
    	/*UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(IesaFakedDataProducer.SALE_001);
    	create(sale);
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(IesaFakedDataProducer.SALE_001), inject(CashRegisterBusiness.class).find(IesaFakedDataProducer.CASH_REGISTER_001));
    	saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCode());
    	assertThat("saleCashRegisterMovement code start with PAIE", StringUtils.startsWith(saleCashRegisterMovement.getCode(), "PAIE"));
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(IesaFakedDataProducer.SALE_002);	
    	sale.getSalableProductCollection().getCost().setValue(new BigDecimal("1000"));
    	create(sale);
    	*/
    	
    	/*
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "150",null, "850","150");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "200",null, "650", "350");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "100",null, "550", "450");
    	saleCashRegisterMovement = companyBusinessTestHelper.updateSaleCashRegisterMovement(saleCashRegisterMovement, "200", "450", "550");
    	*/
    	
    	//companyBusinessTestHelper.deleteSaleCashRegisterMovement(saleCashRegisterMovement,"650", "350");
    }
         
}
