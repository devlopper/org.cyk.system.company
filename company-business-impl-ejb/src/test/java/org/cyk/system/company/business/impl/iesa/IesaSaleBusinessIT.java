package org.cyk.system.company.business.impl.iesa;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.system.root.model.file.FileRepresentationType;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.file.FileRepresentationTypeDao;
import org.cyk.system.root.persistence.api.file.report.ReportTemplateDao;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.cyk.utility.common.computation.DataReadConfiguration;
import org.cyk.utility.common.generator.AbstractGeneratable;
import org.junit.Test;

public class IesaSaleBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private static String CASH_REGISTER_001 = "CR001",CASH_REGISTER_002 = "CR002",CASH_REGISTER_003 = "CR003";
    private static String SALE_001 = "S001",SALE_002 = "S002";
    private static String CUSTOMER_001 = "C001",CUSTOMER_002 = "C002";
    private Sale sale;
    private SaleCashRegisterMovement saleCashRegisterMovement;
    private SalableProductCollection salableProductCollection;
    private SalableProductCollectionItem salableProductCollectionItem;
    
    @Override
    protected void populate() {
    	AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
    	PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/salecashregistermovementlogo.png";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
    	super.populate();
    	create(inject(TangibleProductBusiness.class).instanciateMany(new String[][]{{"TP01","Books Package Primary"},{"TP02", "Polo shirt Primary"}
    		,{"TP03", "Sportswear Primary"},{"TP04","ID Card"},{"TP05","School Uniform (Up and Down) Primary"},{"TP06","Culottes Primary"}}));
    	create(inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{{"IP01","Re-registration"},{"IP02", "Tuition fees"},{"IP03", "Exam (STA)"}
    		,{"IP04","UCMAS Program"},{"IP05","Swimming (First, Second & Third Terms)"},{"IP06","Art and Craft (First, Second & Third Terms)"}
    		,{"IP07","Transportation (till June 2017)"}}));
    	create(inject(SalableProductBusiness.class).instanciateMany(new String[][]{{"","","","","","","","","","","TP01","60000"}
    		,{"","","","","","","","","","","TP02", "7000"},{"","","","","","","","","","","TP03", "7000"},{"","","","","","","","","","","TP04", "4000"}
    		,{"","","","","","","","","","","TP05", "14000"},{"","","","","","","","","","","TP06", "7000"},{"","","","","","","","","","","IP01", "65000"}
    		,{"","","","","","","","","","","IP02", "1450000"},{"","","","","","","","","","","IP03", "45000"},{"","","","","","","","","","","IP04", "40000"}
    		,{"","","","","","","","","","","IP05", "30000"},{"","","","","","","","","","","IP06", "30000"},{"","","","","","","","","","","IP07", "30000"}}));
    	
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
    	
    	create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001));
    	create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002));
    }
    
    //@Test
    public void crudSalableProductCollection(){
    	salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC001", new Object[][]{
    		{"TP01",1},{"TP03",2}	
    	}, "550");
    	
    	companyBusinessTestHelper.deleteSalableProductCollection(salableProductCollection);
    }
    
    //@Test
    public void crudSalableProductCollectionItem(){
    	salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC002", new Object[][]{}, "0");
    	SalableProductCollectionItem salableProductCollectionItemTP01 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP01",1}, "100");
    	SalableProductCollectionItem salableProductCollectionItemTP02 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP02",2}, "400");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP01, "300");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",3}, "975");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP02, "675");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",1}, "900");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItem, "675");
    }
    
    @Test
    public void crudSale(){
    	sale = companyBusinessTestHelper.createSale(null,CUSTOMER_001, new Object[][]{{"IP01",1},{"IP02",1,100000},{"TP01",1},{"TP02",1},{"TP03",1},{"TP04",1},{"IP03",1},{"TP05",2},{"TP06",1}
    	,{"IP04",3},{"IP05",3},{"IP06",3},{"IP07",10}}, "2173000", "2173000");
    	
    	CreateReportFileArguments<Sale> createSaleReportFileArguments = new CreateReportFileArguments<Sale>(sale);
    	createSaleReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.INVOICE));	
    	inject(SaleBusiness.class).createReportFile(createSaleReportFileArguments);
    	
    	String fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.INVOICE;
		FileRepresentationType fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, sale).iterator().next());
    	
    	//sale = companyBusinessTestHelper.createSale(null, new Object[][]{}, "0", "0");
    	String saleCode = sale.getCode();
    	assertThat("Sale "+saleCode+" exists", inject(SaleBusiness.class).find(saleCode)!=null);
    	assertThat("Salable product collection "+saleCode+" exists", inject(SalableProductCollectionBusiness.class).find(saleCode)!=null);
    	assertThat("Sale code start with FACT", StringUtils.startsWith(saleCode, "FACT"));
    	
    	SaleCashRegisterMovement saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "15000",new String[][]{
    		{"IP01","15000"}
    	}, "2158000","15000");
    	CreateReportFileArguments<SaleCashRegisterMovement> createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
    	
		saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "550000",new String[][]{
    		{"IP01","50000"},{"IP02","500000"}
    	}, "1608000","565000");
    	createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
		
		saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "920000",new String[][]{
    		{"IP02","500000"},{"TP01","60000"},{"IP04","100000"},{"IP05","90000"},{"IP06","90000"},{"IP07","80000"}
    	}, "688000","1485000");
    	createSaleCashRegisterMovementReportFileArguments = new CreateReportFileArguments<SaleCashRegisterMovement>(saleCashRegisterMovement);
    	createSaleCashRegisterMovementReportFileArguments.setLocale(Locale.ENGLISH);
    	createSaleCashRegisterMovementReportFileArguments.setReportTemplate(inject(ReportTemplateDao.class).read(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT));	
    	inject(SaleCashRegisterMovementBusiness.class).createReportFile(createSaleCashRegisterMovementReportFileArguments);
    	
    	fileRepresentationTyeCode = CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT;
		fileRepresentationType = inject(FileRepresentationTypeDao.class).read(fileRepresentationTyeCode);		
		companyBusinessTestHelper.write(inject(FileBusiness.class).findByRepresentationTypeByIdentifiable(fileRepresentationType, saleCashRegisterMovement).iterator().next());
		
		DataReadConfiguration dataReadConfiguration = new DataReadConfiguration();
		dataReadConfiguration.setGlobalFilter("x");
		assertEquals(1l, inject(SaleBusiness.class).countAll());
		
		dataReadConfiguration.setGlobalFilter("F");
		
		assertEquals(1l, inject(SaleBusiness.class).countByString((String)null));
		assertEquals(1l, inject(SaleBusiness.class).countByString(""));
		
		assertEquals(1l, inject(SaleBusiness.class).countByString("F"));
		
		assertEquals(0l, inject(SaleBusiness.class).countByString("X"));
		
    	//companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "200", "200","350");
    	
    	//companyBusinessTestHelper.deleteSale(sale);
    }
    
    //@Test
    public void crudSaleCashRegisterMovement(){
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_001);
    	create(sale);
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(SALE_001), inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCashRegisterMovement().getCode());
    	assertThat("saleCashRegisterMovement code start with PAIE", StringUtils.startsWith(saleCashRegisterMovement.getCode(), "PAIE"));
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_002);	
    	sale.getSalableProductCollection().getCost().setValue(new BigDecimal("1000"));
    	create(sale);
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "150",null, "850","150");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "200",null, "650", "350");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "100",null, "550", "450");
    	saleCashRegisterMovement = companyBusinessTestHelper.updateSaleCashRegisterMovement(saleCashRegisterMovement, "200", "450", "550");
    	companyBusinessTestHelper.deleteSaleCashRegisterMovement(saleCashRegisterMovement,"650", "350");
    }
         
}
