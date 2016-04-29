package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanResourceBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetCellBusiness;
import org.cyk.system.company.business.api.production.ProductionUnitBusiness;
import org.cyk.system.company.business.api.production.ResellerBusiness;
import org.cyk.system.company.business.api.production.ResellerProductionBusiness;
import org.cyk.system.company.business.api.production.ResellerProductionPlanBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementInputBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementOutputBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.FormatterBusiness;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.api.security.UserAccountBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.AbstractFormatter;
import org.cyk.system.root.business.impl.BusinessServiceProvider;
import org.cyk.system.root.business.impl.BusinessServiceProvider.Service;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.ContentType;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.geography.ContactCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.system.root.persistence.api.security.RoleDao;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.computation.DataReadConfiguration;
import org.joda.time.DateTime;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static CompanyBusinessLayer INSTANCE;
	
	private String pointOfSaleInvoiceReportName;
	private String pointOfSalePaymentReportName;
	private final String pointOfSaleReportExtension = "pdf";
	
	private final String actionCreateSaleCashRegisterMovementInput = "acscrmi";
	private final String actionCreateSaleCashRegisterMovementOutput = "acscrmo";
	
	public static final Integer PRODUCT_POINT_OF_SALE = 1000;
	public static final Integer PRODUCT_TANGIBLE_SALE_STOCK = 1001;
	public static final Integer PRODUCT_INTANGIBLE_SALE_STOCK = 1002;
	
	public static final Integer STRUCTURE_COMPANY = 2000;
	public static final Integer FILE_COMPANY_LOGO = 3000;
	public static final Integer ACCOUNTING_PERIOD = 4000;
	
	private DivisionType departmentDivisiontype;
	
	@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SalableProductBusiness salableProductBusiness;
	@Inject private SaleProductBusiness saleProductBusiness;
	@Inject private SaleStockTangibleProductMovementInputBusiness saleStockInputBusiness;
	@Inject private SaleStockTangibleProductMovementOutputBusiness saleStockOutputBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private FileBusiness fileBusiness;
	@Inject private DivisionTypeBusiness divisionTypeBusiness;
	@Inject private DivisionBusiness divisionBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private StockTangibleProductMovementBusiness stockTangibleProductMovementBusiness;
	@Inject private StockableTangibleProductBusiness stockableTangibleProductBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private ProductionBusiness productionBusiness;
	@Inject private ProductionUnitBusiness productionUnitBusiness;
	@Inject private ProductionSpreadSheetCellBusiness productionInputBusiness;
	@Inject private ProductionPlanBusiness productionPlanBusiness;
	@Inject private ProductionPlanResourceBusiness productionPlanResourceBusiness;
	@Inject private ResellerBusiness resellerBusiness;
	@Inject private CashRegisterMovementBusiness cashRegisterMovementBusiness;
	
	@Inject private ResellerProductionPlanBusiness resellerProductionPlanBusiness;
	@Inject private ResellerProductionBusiness resellerProductionBusiness;
	//@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	//@Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
	@Inject private UserAccountBusiness userAccountBusiness;
	@Inject private RoleDao roleDao;
	@Inject private PersonDao personDao;
	@Inject private SalableProductDao salableProductDao;
	@Inject private StockableTangibleProductDao stockableTangibleProductDao;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	@Inject private StringGeneratorBusiness stringGeneratorBusiness;
	@Setter private CompanyReportProducer companyReportProducer = new DefaultSaleReportProducer();
	//private Role roleSaleManager,roleStockManager,roleHumanResourcesManager,customerManager,productionManager;
	
	@Inject private CompanyReportRepository companyReportRepository;
	@Inject private FormatterBusiness formatterBusiness;
	
	private static final Collection<CompanyBusinessLayerListener> COMPANY_BUSINESS_LAYER_LISTENERS = new ArrayList<>();
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		formatterBusiness.registerFormatter(Production.class, new AbstractFormatter<Production>() {
			private static final long serialVersionUID = 3952155697329951912L;
			@Override
			public String format(Production production, ContentType contentType) {
				return rootBusinessLayer.getTimeBusiness().formatDate(production.getPeriod().getFromDate());
			}
		});
		
		pointOfSaleInvoiceReportName = RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.pointofsale.invoice");
		pointOfSalePaymentReportName = RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.pointofsale.paymentreceipt");
		
		ApplicationBusinessImpl.Listener.COLLECTION.add(new ApplicationBusinessImpl.Listener.Adapter.Default(){
			private static final long serialVersionUID = 5234235361543643487L;
			@Override
			public void installationEnded(Installation installation) {
				super.installationEnded(installation);
				OwnedCompany ownedCompany = ownedCompanyBusiness.findDefaultOwnedCompany();
				Collection<Person> persons = personDao.select().all();
				ownedCompany.getCompany().setManager(persons.isEmpty() ? null : persons.iterator().next());
				companyBusiness.update(ownedCompany.getCompany());
				CashRegister cashRegister = create(new CashRegister("CashRegister01",ownedCompany,createMovementCollection("CashRegisterMovementCollection01", "Entrée", "Sortie")));
				cashierBusiness.create(new Cashier(ownedCompany.getCompany().getManager(),cashRegister));
			}
		});
		
		BusinessServiceProvider.Identifiable.COLLECTION.add(new AbstractActorBusinessImpl.BusinessServiceProviderIdentifiable<Customer,Customer.SearchCriteria>(Customer.class){
			private static final long serialVersionUID = 1322416788278558869L;
			
			@Override
			protected Customer.SearchCriteria createSearchCriteria(Service service,DataReadConfiguration dataReadConfiguration) {
				return new Customer.SearchCriteria(dataReadConfiguration.getGlobalFilter());
			}
        });
		
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
		
		create(new IntangibleProduct(IntangibleProduct.STOCKING, IntangibleProduct.STOCKING, null, null));
    	create(new TangibleProduct(TangibleProduct.STOCKING, TangibleProduct.STOCKING, null, null));
    	
    	create(new SalableProduct(getEnumeration(IntangibleProduct.class, IntangibleProduct.STOCKING), null));
    	create(new StockableTangibleProduct(getEnumeration(TangibleProduct.class, TangibleProduct.STOCKING)
    			, rootDataProducerHelper.createMovementCollection(TangibleProduct.STOCKING, "Input", "Output")));
	}
	
	private void company(){ 
		ReportTemplate pointOfSaleReportTemplate = new ReportTemplate("POINT_OF_SALE",createFile("report/payment/pos_a4.jrxml", "pointofsale.jrxml"),null,null);
		create(pointOfSaleReportTemplate);
		
		/*File pointOfSaleReportFile = new File();*/
		byte[] bytes = null;
		/*for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS){
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
		*/
		Company company = new Company();
		company.setCode("C01");
		String companyName = null;
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS){
			String value = listener.getCompanyName();
			if(value!=null)
				companyName = value;
		}
		company.setName(companyName==null?"MyCompany":companyName);
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
		company.setContactCollection(new ContactCollection());
		//company.getContactCollection().setPhoneNumbers(new ArrayList<PhoneNumber>());
		//RootRandomDataProvider.getInstance().phoneNumber(company.getContactCollection());
		
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handleCompanyToInstall(company);
		installObject(STRUCTURE_COMPANY,companyBusiness,company);
		
		OwnedCompany ownedCompany = new OwnedCompany();
		ownedCompany.setCompany(company);
		ownedCompany.setSelected(Boolean.TRUE);
		//ownedCompanyBusiness.create(ownedCompany);
		installObject(-1,ownedCompanyBusiness,ownedCompany);
		
		FiniteStateMachine finiteStateMachine = rootDataProducerHelper.createFiniteStateMachine("SALE_FINITE_MACHINE_STATE"
    			, new String[]{"SALE_FINITE_MACHINE_ALPHABET_VALID"}
    		, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
    		, Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}, new String[][]{
    			{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,"SALE_FINITE_MACHINE_ALPHABET_VALID",Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
    	});
		
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setOwnedCompany(ownedCompany);
		Integer currentYear = new DateTime().getYear();
		accountingPeriod.setPeriod(new Period(new DateTime(currentYear, 1, 1, 0, 0).toDate(), new DateTime(currentYear, 12, 31, 23, 59).toDate()));
		
		accountingPeriod.getSaleConfiguration().setPointOfSaleReportTemplate(pointOfSaleReportTemplate);
		accountingPeriod.getSaleConfiguration().setValueAddedTaxRate(BigDecimal.ZERO);
		accountingPeriod.getSaleConfiguration().setIdentifierGenerator(stringGenerator("FACT","0", 8l, null, null,8l));
		accountingPeriod.getSaleConfiguration().setCashRegisterMovementIdentifierGenerator(stringGenerator("PAIE","0", 8l, null, null,8l));
		accountingPeriod.getSaleConfiguration().setFiniteStateMachine(finiteStateMachine);
		
		stringGeneratorBusiness.create(accountingPeriod.getSaleConfiguration().getIdentifierGenerator());
		stringGeneratorBusiness.create(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator());
		//accountingPeriodBusiness.create(accountingPeriod);
		for(CompanyBusinessLayerListener listener : COMPANY_BUSINESS_LAYER_LISTENERS)
			listener.handleAccountingPeriodToInstall(accountingPeriod);
		installObject(ACCOUNTING_PERIOD,accountingPeriodBusiness,accountingPeriod);
		
		
		
		
		//intangibleProductBusiness.create(new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null, null));
		//installObject(PRODUCT_INTANGIBLE_SALE_STOCK,intangibleProductBusiness,new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null));
		
		//tangibleProductBusiness.create(new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null, null));
		//installObject(PRODUCT_TANGIBLE_SALE_STOCK,tangibleProductBusiness,new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null));
	}
	
	private void security(){ }
		
	private void structure(){
		DivisionType department = new DivisionType(null, DivisionType.DEPARTMENT, "Department");
        create(department);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {
        beansMap.put((Class)Employee.class, (TypedBusiness)employeeBusiness);
        beansMap.put((Class)Customer.class, (TypedBusiness)customerBusiness);
        beansMap.put((Class)Reseller.class, (TypedBusiness)resellerBusiness);
        beansMap.put((Class)ResellerProduction.class, (TypedBusiness)resellerProductionBusiness);
        beansMap.put((Class)ResellerProductionPlan.class, (TypedBusiness)resellerProductionPlanBusiness);
        beansMap.put((Class)TangibleProduct.class, (TypedBusiness)tangibleProductBusiness);
        beansMap.put((Class)IntangibleProduct.class, (TypedBusiness)intangibleProductBusiness);
        beansMap.put((Class)Product.class, (TypedBusiness)productBusiness);
        beansMap.put((Class)DivisionType.class, (TypedBusiness)divisionTypeBusiness);
        beansMap.put((Class)Division.class, (TypedBusiness)divisionBusiness);
        beansMap.put((Class)ProductCollection.class, (TypedBusiness)productCollectionBusiness);
        beansMap.put((Class)Sale.class, (TypedBusiness)saleBusiness);
        beansMap.put((Class)SaleCashRegisterMovement.class, (TypedBusiness)saleCashRegisterMovementBusiness);
        beansMap.put((Class)TangibleProductInventory.class, (TypedBusiness)tangibleProductInventoryBusiness); 
        beansMap.put((Class)StockableTangibleProduct.class, (TypedBusiness)stockableTangibleProductBusiness);
        beansMap.put((Class)StockTangibleProductMovement.class, (TypedBusiness)stockTangibleProductMovementBusiness);
        beansMap.put((Class)ProductCategory.class, (TypedBusiness)productCategoryBusiness);
        beansMap.put((Class)OwnedCompany.class, (TypedBusiness)ownedCompanyBusiness);
        beansMap.put((Class)Company.class, (TypedBusiness)companyBusiness);
        beansMap.put((Class)SaleStockTangibleProductMovementInput.class, (TypedBusiness)saleStockInputBusiness);
        beansMap.put((Class)SaleStockTangibleProductMovementOutput.class, (TypedBusiness)saleStockOutputBusiness);
        beansMap.put((Class)Production.class, (TypedBusiness)productionBusiness);
        beansMap.put((Class)ProductionUnit.class, (TypedBusiness)productionUnitBusiness);
        beansMap.put((Class)ProductionValue.class, (TypedBusiness)productionInputBusiness);
        beansMap.put((Class)ProductionPlan.class, (TypedBusiness)productionPlanBusiness);
        beansMap.put((Class)ProductionPlanResource.class, (TypedBusiness)productionPlanResourceBusiness);
        beansMap.put((Class)CashRegister.class, (TypedBusiness)cashRegisterBusiness);
        beansMap.put((Class)CashRegisterMovement.class, (TypedBusiness)cashRegisterMovementBusiness);
    }
	
	/**/
	
	@Override
	protected void setConstants(){
    	departmentDivisiontype = divisionTypeBusiness.find(DivisionType.DEPARTMENT);
    }
	
	public IntangibleProduct getIntangibleProductStocking(){
		return getEnumeration(IntangibleProduct.class,IntangibleProduct.STOCKING);
	}
	public TangibleProduct getTangibleProductStocking(){
		return getEnumeration(TangibleProduct.class,TangibleProduct.STOCKING);
	}
	public SalableProduct getSalableProductStocking(){
		return salableProductDao.readByProduct(getIntangibleProductStocking());
	}
	public StockableTangibleProduct getStockableTangibleProductStocking(){
		return stockableTangibleProductDao.readByTangibleProduct(getTangibleProductStocking());
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
		
	/**/
	
	protected void fakeTransactions(){}
	
	/**/
	
}
