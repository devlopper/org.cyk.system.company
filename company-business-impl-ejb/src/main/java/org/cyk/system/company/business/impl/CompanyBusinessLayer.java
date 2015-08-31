package org.cyk.system.company.business.impl;

import static org.cyk.system.company.business.api.CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFIER;
import static org.cyk.system.company.business.api.CompanyBusinessLayerListener.SALE_IDENTIFIER;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

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
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
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
import org.cyk.system.root.model.generator.ValueGenerator.GenerateMethod;
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

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER)
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static CompanyBusinessLayer INSTANCE;
	
	@Getter private final String roleSaleManagerCode = "SALEMANAGER",roleStockManagerCode = "STOCKMANAGER",roleHumanResourcesManagerCode = "HUMANRESOURCESMANAGER"
			,roleCustomerManagerCode = "CUSTOMERMANAGER",roleProductionManagerCode="PRODUCTIONMANAGER";
	
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
	private ApplicationBusiness applicationBusiness = RootBusinessLayer.getInstance().getApplicationBusiness();
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
		
		StringValueGenerator<Sale> saleStringValueGenerator = new StringValueGenerator<Sale>(SALE_IDENTIFIER,"", Sale.class);
		applicationBusiness.registerValueGenerator((ValueGenerator<?, ?>) saleStringValueGenerator);
		saleStringValueGenerator.setPrefix("Pref");
		saleStringValueGenerator.setSuffix("Suff");
		saleStringValueGenerator.setMethod(new GenerateMethod<Sale, String>() {
			@Override
			public String execute(Sale sale) {
				return sale.getIdentifier().toString();
			}
		});
		
		StringValueGenerator<CashRegisterMovement> cashRegisterMovementStringValueGenerator = new StringValueGenerator<CashRegisterMovement>(CASH_MOVEMENT_IDENTIFIER,"", CashRegisterMovement.class);
		applicationBusiness.registerValueGenerator((ValueGenerator<?, ?>) cashRegisterMovementStringValueGenerator);
		cashRegisterMovementStringValueGenerator.setPrefix("Paie");
		cashRegisterMovementStringValueGenerator.setMethod(new GenerateMethod<CashRegisterMovement, String>() {
			@Override
			public String execute(CashRegisterMovement cashRegisterMovement) {
				return cashRegisterMovement.getIdentifier().toString();
			}
		});
						
		/*
		AbstractIdentifiableLifeCyleEventListener.MAP.put(Sale.class, new LongIdentifiableLifeCyleEventAdapter(){
			private static final long serialVersionUID = 1695079730020340429L;
			@Override
			public void onPrePersist(AbstractIdentifiable identifiable) {
				((Sale)identifiable).setIdentificationNumber(
						applicationBusiness.generateStringValue(SALE_IDENTIFICATION_NUMBER,(Sale)identifiable)
						);
				
			}
		});
		
		AbstractIdentifiableLifeCyleEventListener.MAP.put(CashRegisterMovement.class, new LongIdentifiableLifeCyleEventAdapter(){
			private static final long serialVersionUID = 1695079730020340429L;
			@Override
			public void onPrePersist(AbstractIdentifiable identifiable) {
				((CashRegisterMovement)identifiable).setIdentificationNumber(
						applicationBusiness.generateStringValue(CASH_MOVEMENT_IDENTIFICATION_NUMBER,(CashRegisterMovement)identifiable)
						);
				
			}
		});
		*/
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
		ReportBasedOnTemplateFile<SaleReport> report = createReport(pointOfSalePaymentReportName+saleCashRegisterMovement.getCashRegisterMovement().getIdentifier(),
				saleCashRegisterMovement.getReport(), 
				saleReport,saleCashRegisterMovement.getSale().getAccountingPeriod().getPointOfSaleReportFile(),
				pointOfSaleReportExtension);
		if(saleCashRegisterMovement.getReport()==null)
			saleCashRegisterMovement.setReport(new File());
		persistReport(saleCashRegisterMovement.getReport(), report);
	}
	
	public void persistPointOfSale(Sale sale,SaleReport saleReport){
		ReportBasedOnTemplateFile<SaleReport> report = createReport(pointOfSaleInvoiceReportName+sale.getIdentifier(),
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
		
        Employee employee = new Employee();
        employee.setPerson(new Person());
        employee.getPerson().setName("Zadi");
        employee.getPerson().setLastName("Gerard");
        employeeBusiness.create(employee);
        
        Role[] roles = roleBusiness.findAllExclude(Arrays.asList(RootBusinessLayer.getInstance().getAdministratorRole())).toArray(new Role[]{});
        userAccountBusiness.create(new UserAccount(employee.getPerson(), new Credentials("zadi", "123"),null,roles));
        create(new Cashier(employee,cashRegister));
        
        Company company = companyBusiness.find().one();
        company.setManager(employee.getPerson());
        companyBusiness.update(company);
                
	}
	
	/**/
	
}
