package org.cyk.system.company.business.impl.iesa;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper.TestCase;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.junit.Test;

public class IesaSaleBusinessIT extends AbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Test
    public void crudSalableProductCollection(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SalableProductCollectionBusiness.class).instanciateOne("SPC001", new String[][]{}));
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "0", "0", "0", "0");
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
    	salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), "1", "60000", "0", "60000");
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
    	
    	salableProductCollection.getItems().getCollection().clear();
    	salableProductCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP01","1","0","0");
    	companyBusinessTestHelper.update(salableProductCollection);
    	salableProductCollection.getItems().getCollection().clear();
    	salableProductCollection = inject(SalableProductCollectionDao.class).read("SPC001");
    	assertEquals(1, inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection).size());
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollection(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P001",null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovement(){
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new String[][]{}));
    	
    	testCase.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("P001",null, IesaFakedDataProducer.CASH_REGISTER_001, new String[][]{}));
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne("P001", "Sale001", "0"));
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementBusiness.class).instanciateOne("P001", "Sale002", "0"));
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleCashRegisterMovementCollectionUserInterface(){
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	TestCase testCase = instanciateTestCase();
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale001",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"TP01",1},{"TP02",2} }));
    	companyBusinessTestHelper.assertSale("Sale001", "74000", "74000");
    	//companyBusinessTestHelper.assertCost(inject(SaleDao.class).read("Sale001").getSalableProductCollection().getCost(), "3", "74000", "0", "74000");
    	
    	//companyBusinessTestHelper.createReportFile(inject(SaleDao.class).read("Sale001"), CompanyConstant.Code.ReportTemplate.INVOICE, Locale.ENGLISH,1);
    	
    	testCase.create(inject(SaleBusiness.class).instanciateOne("Sale002",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{ {"IP01",4},{"IP02",3} }));
    	companyBusinessTestHelper.assertSale("Sale002", "4610000", "4610000");
    	
    	SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne(userAccount);
    	saleCashRegisterMovementCollection.setCode("P001");
    	
    	inject(SaleCashRegisterMovementCollectionBusiness.class).setCashRegister(userAccount, saleCashRegisterMovementCollection, inject(CashRegisterDao.class)
    			.read(IesaFakedDataProducer.CASH_REGISTER_001));
    	
    	inject(SaleCashRegisterMovementBusiness.class).instanciateOne(saleCashRegisterMovementCollection, "Sale001", "500");
    	
    	inject(SaleCashRegisterMovementBusiness.class).instanciateOne(saleCashRegisterMovementCollection, "Sale002", "800");
    	
    	testCase.create(saleCashRegisterMovementCollection);
    	companyBusinessTestHelper.assertSaleCashRegisterMovementCollection("P001", "1300", "1300", null, null, null, "1300");
    	
    	//companyBusinessTestHelper.createReportFile(saleCashRegisterMovementCollection, CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4
    	//		, Locale.ENGLISH,1);
    	
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
    	
    	testCase.clean();
    }
    
    /* Complex Crud */
    
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
