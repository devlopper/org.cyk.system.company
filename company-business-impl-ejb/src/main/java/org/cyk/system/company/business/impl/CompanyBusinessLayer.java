package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.SaleReportProducer;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetCellBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateRowBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateRow;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.security.RoleBusiness;
import org.cyk.system.root.business.api.security.UserAccountBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.AbstractReport;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.generator.StringValueGenerator;
import org.cyk.system.root.model.generator.ValueGenerator;
import org.cyk.system.root.model.geography.PhoneNumber;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Credentials;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.security.Role;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.model.time.Period;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.Setter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER)
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static CompanyBusinessLayer INSTANCE;
	
	@Getter private final String roleSaleManagerCode = "SALEMANAGER",roleStockManagerCode = "STOCKMANAGER",roleHumanResourcesManagerCode = "HUMANRESOURCESMANAGER"
			,roleCustomerManagerCode = "CUSTOMERMANAGER",roleProductionManagerCode="PRODUCTIONMANAGER";
	/*
	@Getter private final String reportPointOfSale = "pos";
	@Getter private final String reportStockDashboard = "rsdb";
	@Getter private final String parameterCustomerReportType = "crt";
	@Getter private final String parameterCustomerReportBalance = "crb";
	@Getter private final String parameterCustomerReportSaleStock = "crss";
	@Getter private final String parameterCustomerBalanceType = "cbt";
	@Getter private final String parameterCustomerBalanceAll = "cball";
	@Getter private final String parameterCustomerBalanceCredence = "cbcred";
	@Getter private final String parameterMinimumRemainingNumberOfGoods = "mrnog";
	@Getter private final String parameterBalanceType = "bt";
	@Getter private final String parameterSaleStockReportType = "crt";
	@Getter private final String parameterSaleStockReportCashRegister = "ssorcr";
	@Getter private final String parameterSaleStockReportInventory = "ssori";
	@Getter private final String parameterSaleStockReportCustomer = "ssorc";
	@Getter private final String parameterSaleStockReportInput = "ssiri";
	@Getter private final String parameterSaleDone = "saledone";
	*/
	@Getter private String pointOfSaleInvoiceReportName;
	@Getter private String pointOfSalePaymentReportName;
	@Getter private final String pointOfSaleReportExtension = "pdf";
	
	public static final Integer PRODUCT_POINT_OF_SALE = 1000;
	public static final Integer PRODUCT_TANGIBLE_SALE_STOCK = 1001;
	public static final Integer PRODUCT_INTANGIBLE_SALE_STOCK = 1002;
	
	public static final Integer STRUCTURE_COMPANY = 2000;
	public static final Integer FILE_COMPANY_LOGO = 3000;
	public static final Integer ACCOUNTING_PERIOD = 4000;
	
	@Getter private DivisionType departmentDivisiontype;
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleStockInputBusiness saleStockInputBusiness;
	@Inject private SaleStockOutputBusiness saleStockOutputBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private FileBusiness fileBusiness;
	@Inject private DivisionTypeBusiness divisionTypeBusiness;
	@Inject private DivisionBusiness divisionBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private ProductionSpreadSheetBusiness productionBusiness;
	@Inject private ProductionSpreadSheetCellBusiness productionInputBusiness;
	@Inject private ProductionSpreadSheetTemplateBusiness productionPlanModelBusiness;
	@Inject private ProductionSpreadSheetTemplateRowBusiness productionPlanModelInputBusiness;
	//@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	//@Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
	@Inject private UserAccountBusiness userAccountBusiness;
	@Inject private RoleBusiness roleBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	@Getter @Setter private SaleReportProducer saleReportProducer = new DefaultSaleReportProducer();
	
	//@Getter private Role roleSaleManager,roleStockManager,roleHumanResourcesManager,customerManager,productionManager;
	@Getter @Setter private TangibleProduct tangibleProductSaleStock;
	@Getter private IntangibleProduct intangibleProductSaleStock;
	private CashRegister cashRegister;
	@Inject private CompanyReportRepository companyReportRepository;
	
	private static final Collection<CompanyBusinessLayerListener> COMPANY_BUSINESS_LAYER_LISTENERS = new ArrayList<>();
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		registerResourceBundle("org.cyk.system.company.model.resources.entity", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.model.resources.message", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.business.impl.resources.message", getClass().getClassLoader());
		
		pointOfSaleInvoiceReportName = RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.pointofsale.invoice");
		pointOfSalePaymentReportName = RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.pointofsale.paymentreceipt");
		
		applicationBusiness.registerValueGenerator((ValueGenerator<?, ?>) new StringValueGenerator<CashRegisterMovement>(
        		CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFICATION_NUMBER,"", CashRegisterMovement.class));
		applicationBusiness.registerValueGenerator((ValueGenerator<?, ?>) new StringValueGenerator<Sale>(
        		CompanyBusinessLayerListener.SALE_IDENTIFICATION_NUMBER,"", Sale.class));
		
	}
	
	@Override
	protected AbstractReportRepository getReportRepository() {
		return companyReportRepository;
	}
	
	@Override
	protected void persistData() {
		security();
		structure();
		company();
	}
	
	private void company(){ 
		File pointOfSaleReportFile = new File();
		byte[] bytes = null;
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS){
			byte[] value = listener.getCompanyPointOfSaleBytes();
			if(value!=null)
				bytes = value;
		}
		if(bytes==null)
			bytes = getResourceAsBytes("report/payment/pos2.jrxml");
		bytes = getResourceAsBytes("report/payment/pos_a4.jrxml");
		
		pointOfSaleReportFile = fileBusiness.process(bytes,"pointofsale.jrxml");
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handlePointOfSaleToInstall(pointOfSaleReportFile);
		
		installObject(PRODUCT_POINT_OF_SALE,fileBusiness,pointOfSaleReportFile);
		
		Company company = new Company();
		company.setCode("C01");
		String companyName = null;
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS){
			String value = listener.getCompanyName();
			if(value!=null)
				companyName = value;
		}
		company.setName(companyName==null?"MyCompany":companyName);
		//company.setImage(fileBusiness.process(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/image/logo.png")),"image.png"));
		bytes = null;
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS){
			byte[] value = listener.getCompanyLogoBytes();
			if(value!=null)
				bytes = value;
		}
		company.setImage(fileBusiness.process(bytes==null?getResourceAsBytes("image/logo.png"):bytes,"companylogo.png"));
		//fileBusiness.create(company.getImage());
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handleCompanyLogoToInstall(company.getImage());
		installObject(FILE_COMPANY_LOGO,fileBusiness,company.getImage());
		company.getContactCollection().setPhoneNumbers(new ArrayList<PhoneNumber>());
		RootRandomDataProvider.getInstance().phoneNumber(company.getContactCollection());
		//PhoneNumber pn = new PhoneNumber();
		//pn.setCollection(company.getContactCollection());
		//pn.setNumber("22441213");
		//pn.setCountry(RootBusinessLayer.getInstance().getCountryCoteDivoire());
		//pn.setType(RootBusinessLayer.getInstance().getLandPhoneNumberType());
		//company.getContactCollection().getPhoneNumbers().add(pn);
		//companyBusiness.create(company);
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handleCompanyToInstall(company);
		installObject(STRUCTURE_COMPANY,companyBusiness,company);
		
		OwnedCompany ownedCompany = new OwnedCompany();
		ownedCompany.setCompany(company);
		ownedCompany.setSelected(Boolean.TRUE);
		//ownedCompanyBusiness.create(ownedCompany);
		installObject(-1,ownedCompanyBusiness,ownedCompany);
		
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setOwnedCompany(ownedCompany);
		accountingPeriod.setPeriod(new Period(new DateTime(2015, 1, 1, 0, 0).toDate(), new DateTime(2015, 12, 31, 23, 59).toDate()));
		accountingPeriod.setPointOfSaleReportFile(pointOfSaleReportFile);
		accountingPeriod.setValueAddedTaxRate(BigDecimal.ZERO);
		//accountingPeriodBusiness.create(accountingPeriod);
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handleAccountingPeriodToInstall(accountingPeriod);
		installObject(ACCOUNTING_PERIOD,accountingPeriodBusiness,accountingPeriod);
		
		cashRegister = create(new CashRegister("CR01",ownedCompany,BigDecimal.ZERO, null, null));
		
		//intangibleProductBusiness.create(new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null, null));
		installObject(PRODUCT_INTANGIBLE_SALE_STOCK,intangibleProductBusiness,new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null, null));
		
		//tangibleProductBusiness.create(new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null, null));
		installObject(PRODUCT_TANGIBLE_SALE_STOCK,tangibleProductBusiness,new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null, null));
	}
	
	private void security(){ 
		createRole(roleSaleManagerCode, "Sale Manager");
    	createRole(roleStockManagerCode, "Stock Manager");
    	createRole(roleHumanResourcesManagerCode, "Human Resources Manager");
    	createRole(roleCustomerManagerCode, "Customer Manager");
    	createRole(roleProductionManagerCode, "Production Manager");
    }
	
	
	private void structure(){
		DivisionType department = new DivisionType(null, DivisionType.DEPARTMENT, "Department");
        create(department);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {
        beansMap.put((Class)Employee.class, (TypedBusiness)employeeBusiness);
        beansMap.put((Class)Customer.class, (TypedBusiness)customerBusiness);
        beansMap.put((Class)TangibleProduct.class, (TypedBusiness)tangibleProductBusiness);
        beansMap.put((Class)Product.class, (TypedBusiness)productBusiness);
        beansMap.put((Class)DivisionType.class, (TypedBusiness)divisionTypeBusiness);
        beansMap.put((Class)Division.class, (TypedBusiness)divisionBusiness);
        beansMap.put((Class)ProductCollection.class, (TypedBusiness)productCollectionBusiness);
        beansMap.put((Class)Sale.class, (TypedBusiness)saleBusiness);
        beansMap.put((Class)SaleCashRegisterMovement.class, (TypedBusiness)saleCashRegisterMovementBusiness);
        beansMap.put((Class)TangibleProductInventory.class, (TypedBusiness)tangibleProductInventoryBusiness); 
        beansMap.put((Class)TangibleProductStockMovement.class, (TypedBusiness)tangibleProductStockMovementBusiness);
        beansMap.put((Class)ProductCategory.class, (TypedBusiness)productCategoryBusiness);
        beansMap.put((Class)OwnedCompany.class, (TypedBusiness)ownedCompanyBusiness);
        beansMap.put((Class)Company.class, (TypedBusiness)companyBusiness);
        beansMap.put((Class)SaleStockInput.class, (TypedBusiness)saleStockInputBusiness);
        beansMap.put((Class)SaleStockOutput.class, (TypedBusiness)saleStockOutputBusiness);
        beansMap.put((Class)ProductionSpreadSheet.class, (TypedBusiness)productionBusiness);
        beansMap.put((Class)ProductionSpreadSheetCell.class, (TypedBusiness)productionInputBusiness);
        beansMap.put((Class)ProductionSpreadSheetTemplate.class, (TypedBusiness)productionPlanModelBusiness);
        beansMap.put((Class)ProductionSpreadSheetTemplateRow.class, (TypedBusiness)productionPlanModelInputBusiness);
    }
	
	/**/
	
	@Override
	protected void setConstants(){
    	departmentDivisiontype = divisionTypeBusiness.find(DivisionType.DEPARTMENT);
    	intangibleProductSaleStock = intangibleProductBusiness.find(IntangibleProduct.SALE_STOCK);
    	tangibleProductSaleStock = tangibleProductBusiness.find(TangibleProduct.SALE_STOCK);
    }
	
	
	public static CompanyBusinessLayer getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Installation buildInstallation() {
		Installation installation = super.buildInstallation();
		installation.setFaked(Boolean.FALSE);
		return installation;
	}
	
	@Override
	public void installApplication() {
		super.installApplication();
	}
	
	public Collection<CompanyBusinessLayerListener> getCompanyBusinessLayerListeners() {
		return COMPANY_BUSINESS_LAYER_LISTENERS;
	}
	
	public void persistPointOfSale(SaleCashRegisterMovement saleCashRegisterMovement,SaleReport saleReport){
		ReportBasedOnTemplateFile<SaleReport> report = createReport(pointOfSalePaymentReportName+saleCashRegisterMovement.getCashRegisterMovement().getIdentificationNumber(),
				saleCashRegisterMovement.getReport(), 
				saleReport,saleCashRegisterMovement.getSale().getAccountingPeriod().getPointOfSaleReportFile(),
				pointOfSaleReportExtension);
		if(saleCashRegisterMovement.getReport()==null)
			saleCashRegisterMovement.setReport(new File());
		persistReport(saleCashRegisterMovement.getReport(), report);
	}
	
	public void persistPointOfSale(Sale sale,SaleReport saleReport){
		ReportBasedOnTemplateFile<SaleReport> report = createReport(pointOfSaleInvoiceReportName+sale.getIdentificationNumber(),
				sale.getReport(), saleReport,sale.getAccountingPeriod().getPointOfSaleReportFile(),
				pointOfSaleReportExtension);
		if(sale.getReport()==null)
			sale.setReport(new File());
		
		persistReport(sale.getReport(), report);
	}
	
	private void persistReport(File file,AbstractReport<?> report){
		file.setBytes(report.getBytes());
		file.setExtension(report.getFileExtension());
		if(file.getIdentifier()==null){
			fileBusiness.create(file);
			//logDebug("Report created");
		}else{
			fileBusiness.update(file);
			//logDebug("Report updated");
		}
	}
	
	public ReportBasedOnTemplateFile<SaleReport> createReport(String name,File file,SaleReport saleReport,File template,String fileExtension){
		ReportBasedOnTemplateFile<SaleReport> report = new ReportBasedOnTemplateFile<SaleReport>();
		report.setTitle(name);
		report.setFileExtension(StringUtils.isBlank(fileExtension)?pointOfSaleReportExtension:fileExtension);
		//report.setFileName(RootBusinessLayer.getInstance().buildReportFileName(report) /*pointOfSaleReportName*/);
		RootBusinessLayer.getInstance().prepareReport(report);
		
		if(saleReport==null){
			report.setBytes(file.getBytes());
		}else{
			report.getDataSource().add(saleReport);
			report.setTemplateFile(template);
			reportBusiness.build(report, Boolean.FALSE);
		}
		return report;
	}
	
	/**/
	
	protected void fakeTransactions(){
		
		ProductCategory productCategoryEntretienCoprs = productCategory(null, "EC", "Entretien du corps");
        ProductCategory productCategoryBeaute = productCategory(null, "BT", "Beaute");
        
        ProductCategory productCategorySoindDuCorps = productCategory(productCategoryEntretienCoprs, "SC", "Soin du coprs");
        ProductCategory productCategoryEpilation = productCategory(productCategoryEntretienCoprs, "EP", "Epilation");
        ProductCategory productCategoryMassage = productCategory(productCategoryEntretienCoprs, "MASS", "Massage");

        ProductCategory productCategoryOnglerie = productCategory(productCategoryBeaute, "OG", "Onglerie");
        ProductCategory productCategoryCoiffure = productCategory(productCategoryBeaute, "CF", "Coiffure");
        
        products(productCategorySoindDuCorps,new String[]{"Gommage","1000","Masque Vert","1000","Masque Argileux","1500"}
        	,new String[]{"Savon noir","1000","Miel","1000","Lait","1000"});
        
        products(productCategoryEpilation,new String[]{"Epilation Permanente","1000","Epilation a la cire","1000","Epilation electrique","1000"}
    	,new String[]{"Rasoir electrique","1000","Cire","1000","Crie chauffante","1000"});
        
        products(productCategoryMassage,new String[]{"Massage relaxant","1000","Massage amincicant","1000","Massage erotique","1000"}
    	,new String[]{"Huile d'arguant","1000","Pierre chaude","1000","Huile d'olive","1000"});
        
        products(productCategoryOnglerie,new String[]{"Manicure","1000","Pedicure","1000","Pose de faux ongle","1000"}
    	,new String[]{"Verni","1000","Coupe ongle","1000","Lime","1000"});
        
        products(productCategoryCoiffure,new String[]{"Tissage","1000","Tresse","1000","Champoing","1000"}
    	,new String[]{"Meche naturelle","1000","Meche synthetique","1000","RemiAir","1000"});
        
        Employee employee = new Employee();
        employee.setPerson(new Person());
        employee.getPerson().setName("Zadi");
        employeeBusiness.create(employee);
        
        Role[] roles = roleBusiness.findAllExclude(Arrays.asList(RootBusinessLayer.getInstance().getAdministratorRole())).toArray(new Role[]{});
        userAccountBusiness.create(new UserAccount(employee.getPerson(), new Credentials("zadi", "123"),null,roles));
        create(new Cashier(employee,cashRegister));
        
        Company company = companyBusiness.find().one();
        company.setManager(employee.getPerson());
        companyBusiness.update(company);
        
        //bakeryFakeTransaction();
        
	}
	
	
	private TangibleProduct fakeTangibleProduct(String code,String name, ProductCategory productCategory, String cost){
		TangibleProduct t = new TangibleProduct(code,name,null,productCategory,cost==null?null:new BigDecimal(cost));
		t.setUseQuantity(new BigDecimal("100000"));
		t.setStockQuantity(new BigDecimal("100000"));
		tangibleProductBusiness.create(t);
		return t;
	}
	
	
	private IntangibleProduct fakeIntangibleProduct(String code,String name, ProductCategory productCategory, String cost){
		IntangibleProduct t = new IntangibleProduct(code,name,null,productCategory,StringUtils.isBlank(cost)?null:new BigDecimal(cost));
		productBusiness.create(t);
		return t;
	}
	
	
	private ProductCategory productCategory(ProductCategory parent,String code,String name){
		return productCategoryBusiness.create(new ProductCategory(parent, code, name));
	}
	
	
	private static int I = 0,J=0;
	
	private void products(ProductCategory productCategory,String[] prestations,String[] articles){
		if(prestations!=null)
			for(int i=0;i<prestations.length;i+=2)
				fakeIntangibleProduct("iprod"+(++I),prestations[i],productCategory,prestations[i+1]);
		
		if(articles!=null)
			for(int i=0;i<articles.length;i+=2)
				fakeTangibleProduct("tprod"+(++J),articles[i],productCategory,articles[i+1]);
	}
	
    
	public static Sale sell(SaleBusiness saleBusiness,ProductDao productDao,AccountingPeriod accountingPeriod,Cashier cashier,Integer day,Integer month,Integer hour,Integer minute,String[] products,String amountIn,String amountOut){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	sale.setAccountingPeriod(accountingPeriod);
    	if(day!=null)
    		sale.setDate(new DateTime(new DateTime(accountingPeriod.getPeriod().getFromDate()).getYear(), month, day, hour, minute).toDate());
    	saleProducts(saleBusiness,productDao,sale, products);
    	saleBusiness.create(sale, new SaleCashRegisterMovement(sale, new CashRegisterMovement(cashier.getCashRegister()),
    			amountIn==null?sale.getCost():new BigDecimal(amountIn), amountOut==null?BigDecimal.ZERO:new BigDecimal(amountOut)));
    	return sale;
    }
    
    
    public static Sale sell(SaleBusiness saleBusiness,ProductDao productDao,AccountingPeriod accountingPeriod,Cashier cashier,Integer day,Integer month,Integer hour,Integer minute,String[] products){
    	return sell(saleBusiness, productDao, accountingPeriod, cashier, day, month, hour, minute, products, null, null);
    }
    
    
    public static void saleProducts(SaleBusiness saleBusiness,ProductDao productDao,Sale sale,String[] products){
    	if(products==null)
    		return;
    	for(int i=0;i<products.length;i+=2){
    		SaleProduct saleProduct = saleBusiness.selectProduct(sale, productDao.read(products[i]));
    		saleProduct.setQuantity(new BigDecimal(products[i+1]));
    		saleBusiness.applyChange(sale, saleProduct);
    	}
    		
    }

    /**/
    
    /* bakery */    
    /*
    private void bakeryFakeTransaction(){
    	ProductionSpreadSheetTemplate productionPlanModel = new ProductionSpreadSheetTemplate("PPM1","Production de pain");
    	productionPlanModel.setTimeDivisionType(RootBusinessLayer.getInstance().getTimeDivisionTypeDay());
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("F", "Farine", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("L", "Levure", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("A", "Ameliorant", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("B", "Bois", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("G", "Gaz", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("C", "Carburant", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("GA", "Glace alimentaire", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("PV", "Pain vendable", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("PNV", "Pain non vendu", null, null)));
    	
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("PLANNED", "Planned Quantity")));
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("FACTORED", "Factored Quantity")));
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("UNUSED", "Unused Quantity")));
    	
    	productionPlanModelBusiness.create(productionPlanModel);
    	
		productionPlanModelBusiness.load(productionPlanModel);
    	ProductionSpreadSheet production = new ProductionSpreadSheet();
    	production.getPeriod().setFromDate(new Date());
    	production.getPeriod().setToDate(production.getPeriod().getFromDate());
    	for(ProductionSpreadSheetTemplateRow input : productionPlanModel.getRows()){
			for(ProductionSpreadSheetTemplateColumn productionPlanModelMetric : productionPlanModel.getColumns()){
				ProductionSpreadSheetCell productionInput = new ProductionSpreadSheetCell(input,productionPlanModelMetric);
				//productionInput.getMetricValue().setInput(productionPlanModelMetric.getInputName());
				production.getCells().add(productionInput);
				productionInput.setValue(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 9999)));
			}
		}
    	productionBusiness.create(production);
    }
    */
    
}
