package org.cyk.system.company.business.impl;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionInputBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanModelBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanModelInputBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.product.CredenceReportTableDetails;
import org.cyk.system.company.business.impl.product.SaleReportTableDetail;
import org.cyk.system.company.business.impl.product.SaleStockInputReportTableDetail;
import org.cyk.system.company.business.impl.product.StockDashBoardReportTableDetails;
import org.cyk.system.company.business.impl.product.TangibleProductInventoryReportTableDetails;
import org.cyk.system.company.business.impl.product.TangibleProductStockMovementLineReport;
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
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.product.TangibleProductStockMovementSearchCriteria;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.security.RoleBusiness;
import org.cyk.system.root.business.api.security.UserAccountBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.file.report.DefaultReportBasedOnDynamicBuilder;
import org.cyk.system.root.business.impl.file.report.ReportBasedOnDynamicBuilderAdapter;
import org.cyk.system.root.business.impl.file.report.jasper.DefaultJasperReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderIdentifiableConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderListener;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFileConfiguration;
import org.cyk.system.root.model.geography.PhoneNumber;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.model.security.Credentials;
import org.cyk.system.root.model.security.Role;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.model.time.Period;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.joda.time.DateTime;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER)
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static CompanyBusinessLayer INSTANCE;
	
	@Getter private final String roleSaleManagerCode = "SALEMANAGER",roleStockManagerCode = "STOCKMANAGER",roleHumanResourcesManagerCode = "HUMANRESOURCESMANAGER"
			,roleCustomerManagerCode = "CUSTOMERMANAGER",roleProductionManagerCode="PRODUCTIONMANAGER";
	@Getter private final String reportPointOfSale = "pos";
	@Getter private final String reportStockDashboard = "rsdb";
	@Getter private final String parameterBalanceType = "bt";
	
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
	@Inject private ProductionBusiness productionBusiness;
	@Inject private ProductionInputBusiness productionInputBusiness;
	@Inject private ProductionPlanModelBusiness productionPlanModelBusiness;
	@Inject private ProductionPlanModelInputBusiness productionPlanModelInputBusiness;
	//@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	//@Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
	@Inject private UserAccountBusiness userAccountBusiness;
	@Inject private RoleBusiness roleBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	
	@Getter private Role roleSaleManager,roleStockManager,roleHumanResourcesManager,customerManager,productionManager;
	@Getter @Setter private TangibleProduct tangibleProductSaleStock;
	@Getter private IntangibleProduct intangibleProductSaleStock;
	private CashRegister cashRegister;
	
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		registerResourceBundle("org.cyk.system.company.model.resources.entity", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.model.resources.message", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.business.impl.resources.message", getClass().getClassLoader());
		
		ReportBasedOnDynamicBuilderListener.GLOBALS.add(new ReportBasedOnDynamicBuilderAdapter(){
        	@Override
        	public void report(ReportBasedOnDynamicBuilder<?> report,ReportBasedOnDynamicBuilderParameters<?> parameters) {
        		parameters.setOwner(ownedCompanyBusiness.findDefaultOwnedCompany().getCompany());
        	}
        });
		
        ReportBasedOnDynamicBuilderListener.IDENTIFIABLE_CONFIGURATIONS.add(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),TangibleProductStockMovement.class,TangibleProductStockMovementLineReport.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new TangibleProductStockMovementLineReport((TangibleProductStockMovement) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				//parameters.setTitle("Rapport de stock");
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				TangibleProductStockMovementSearchCriteria searchCriteria = new TangibleProductStockMovementSearchCriteria(fromDate,toDate);
				
				return tangibleProductStockMovementBusiness.findByCriteria(searchCriteria);
			}
		});
        
        ReportBasedOnDynamicBuilderListener.IDENTIFIABLE_CONFIGURATIONS.add(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),Sale.class,SaleReportTableDetail.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new SaleReportTableDetail((Sale) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				SaleSearchCriteria searchCriteria = new SaleSearchCriteria(fromDate,toDate);
				
				return saleBusiness.findByCriteria(searchCriteria);
			}
		});
        
        ReportBasedOnDynamicBuilderListener.IDENTIFIABLE_CONFIGURATIONS.add(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),SaleStockInput.class,SaleStockInputReportTableDetail.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new SaleStockInputReportTableDetail((SaleStockInput) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				DefaultSearchCriteria searchCriteria = new DefaultSearchCriteria(fromDate,toDate);
				
				return saleStockInputBusiness.findByCriteria(searchCriteria);
			}
		});
        
        ReportBasedOnDynamicBuilderListener.IDENTIFIABLE_CONFIGURATIONS.add(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),TangibleProductInventory.class,TangibleProductInventoryReportTableDetails.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new TangibleProductInventoryReportTableDetails((TangibleProductInventoryDetail) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				BusinessEntityInfos tangibleProductInventoryEntityInfos = RootBusinessLayer.getInstance().getApplicationBusiness().findBusinessEntityInfos(TangibleProductInventory.class);
				Long tangibleProductInventoryId = Long.parseLong(parameters.getExtendedParameterMap().get(tangibleProductInventoryEntityInfos.getIdentifier())[0]);
				TangibleProductInventory tangibleProductInventory = tangibleProductInventoryBusiness.load(tangibleProductInventoryId);
				parameters.setTitle(languageBusiness.findClassLabelText(TangibleProductInventory.class)+" "+ RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(tangibleProductInventory.getDate()));
				return tangibleProductInventory.getDetails();
			}
		});
        
        ReportBasedOnDynamicBuilderListener.IDENTIFIABLE_CONFIGURATIONS.add(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),Customer.class,CredenceReportTableDetails.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new CredenceReportTableDetails((Customer) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.credence.title"));
				return customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
			}
		});
		
		registerReportConfiguration(new ReportBasedOnTemplateFileConfiguration<Sale, ReportBasedOnTemplateFile<SaleReport>>(reportPointOfSale) {
			@SuppressWarnings("unchecked")
			@Override
			public <MODEL> ReportBasedOnTemplateFile<SaleReport> build(Class<MODEL> arg0, Collection<MODEL> sales, String arg2,Boolean arg3, Map<String, String[]> arg4) {
				return saleBusiness.findReport((Collection<Sale>) sales);
			}
		});
		
		registerReportConfiguration(new ReportBasedOnDynamicBuilderConfiguration<StockDashBoardReportTableDetails, ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>>(reportStockDashboard) {        	
			@Override
			public ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails> build(ReportBasedOnDynamicBuilderParameters<StockDashBoardReportTableDetails> parameters) {
				parameters.setDatas(new ArrayList<StockDashBoardReportTableDetails>());
				for(TangibleProduct tangibleProduct : tangibleProductBusiness.findAll())
					parameters.getDatas().add(new StockDashBoardReportTableDetails(tangibleProduct) ) ;
		        
				parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.dashboard.stock.title"));
				parameters.setModelClass(StockDashBoardReportTableDetails.class);
				parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder());
		        parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultJasperReportBasedOnDynamicBuilder());
				return (ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>) reportBusiness.build(parameters);
			}
		});
		
	}
	
	
	@Override
	protected void persistData() {
		security();
		structure();
		company();
	}
	
	
	private void company(){ 
		File pointOfSaleReportFile = new File();
    	try {
    		pointOfSaleReportFile = fileBusiness.process(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/report/payment/pos1.jrxml")),
    				"pos1.jrxml");
    		fileBusiness.create(pointOfSaleReportFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Company company = new Company();
		company.setCode("C01");
		company.setName("MyCompany");
		try {
			company.setImage(fileBusiness.process(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/image/logo.png")),"image.png"));
			fileBusiness.create(company.getImage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		company.getContactCollection().setPhoneNumbers(new ArrayList<PhoneNumber>());
		PhoneNumber pn = new PhoneNumber();
		pn.setCollection(company.getContactCollection());
		pn.setNumber("00000000");
		pn.setCountry(RootBusinessLayer.getInstance().getCountryCoteDivoire());
		pn.setType(RootBusinessLayer.getInstance().getLandPhoneNumberType());
		company.getContactCollection().getPhoneNumbers().add(pn);
		companyBusiness.create(company);
		
		OwnedCompany ownedCompany = new OwnedCompany();
		ownedCompany.setCompany(company);
		ownedCompany.setSelected(Boolean.TRUE);
		ownedCompanyBusiness.create(ownedCompany);
		
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setOwnedCompany(ownedCompany);
		accountingPeriod.setPeriod(new Period(new DateTime(2015, 1, 1, 0, 0).toDate(), new DateTime(2015, 12, 31, 23, 59).toDate()));
		accountingPeriod.setPointOfSaleReportFile(pointOfSaleReportFile);
		accountingPeriod.setValueAddedTaxRate(BigDecimal.ZERO);
		accountingPeriodBusiness.create(accountingPeriod);
		
		cashRegister = create(new CashRegister("CR01",ownedCompany,BigDecimal.ZERO, null, null));
		
		intangibleProductBusiness.create(new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null, null));
		tangibleProductBusiness.create(new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null, null));
	}
	
	private void security(){ 
    	createRole(roleSaleManager = new Role(roleSaleManagerCode, "Sale Manager"));
    	createRole(roleStockManager = new Role(roleStockManagerCode, "Stock Manager"));
    	createRole(roleHumanResourcesManager = new Role(roleHumanResourcesManagerCode, "Human Resources Manager"));
    	createRole(customerManager = new Role(roleCustomerManagerCode, "Customer Manager"));
    	createRole(productionManager = new Role(roleProductionManagerCode, "Production Manager"));
    }
	
	
	private void structure(){
		DivisionType department = new DivisionType(null, DivisionType.DEPARTMENT, "Department");
        create(department);
    }
	
	/*
	private void payment(){
		File pointOfSaleReportFile;
    	try {
    		pointOfSaleReportFile=fileBusiness.process(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/report/payment/pos1.jrxml")),
    				"pos1.jrxml");
    		pointOfSaleReportFile.setExtension("jrxml");
    		fileBusiness.create(pointOfSaleReportFile);
    		//genericDao.create(pointOfSaleReportFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
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
        beansMap.put((Class)Production.class, (TypedBusiness)productionBusiness);
        beansMap.put((Class)ProductionInput.class, (TypedBusiness)productionInputBusiness);
        beansMap.put((Class)ProductionPlanModel.class, (TypedBusiness)productionPlanModelBusiness);
        beansMap.put((Class)ProductionPlanModelInput.class, (TypedBusiness)productionPlanModelInputBusiness);
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
	
	
	/**/
	
	protected void fakeTransactions(){
		
		/*
		ProductCategory productCategoryCoiffure = productCategory(null, "COIFFURE", "Coiffure");
        ProductCategory productCategoryEsthetique = productCategory(null, "ESTHETIQUE", "Esthetique");
        ProductCategory productCategoryOnglerie = productCategory(null, "ONGLERIE", "Onglerie");
        ProductCategory productCategoryMassage = productCategory(null, "MASSAGE", "Massage");
		*/
		
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
        
        //ProductCategory pc = productCategory(null, "MM", "Marchandise");
        //products(pc,new String[]{"Depot marchandise",""},null);
        
        /*
        IntangibleProduct ip1 = fakeIntangibleProduct("iprod01","Soin de visage",productCategory2,new BigDecimal("1000"));
        //create(new IntangibleProductDetails(ip1, tp1,tp4));
        fakeIntangibleProduct("iprod02","Tissage",productCategory3,new BigDecimal("3000")));
        fakeIntangibleProduct("iprod03","Pedicure",productCategory1,new BigDecimal("1500")));
        IntangibleProduct ip4 = fakeIntangibleProduct("iprod04","Manicure",dept3,productCategory1,new BigDecimal("1000")));
        
        ProductCollection pc = new ProductCollection("pack01", "Mains-Pieds", dept3, null, new BigDecimal("2000"));
        pc.getCollection().add(ip1);
        pc.getCollection().add(ip4);
        productBusiness.create(pc);
        */
        
        Employee employee = new Employee();
        employee.setPerson(new Person());
        employee.getPerson().setName("Zadi");
        employeeBusiness.create(employee);
        
        Role[] roles = roleBusiness.findAllExclude(Arrays.asList(RootBusinessLayer.getInstance().getAdministratorRole())).toArray(new Role[]{});
        userAccountBusiness.create(new UserAccount(employee.getPerson(), new Credentials("zadi", "123"),null,roles));
        create(new Cashier(employee,cashRegister));
        
        bakeryFakeTransaction();
        
	}
	
	
	private TangibleProduct fakeTangibleProduct(String code,String name, ProductCategory productCategory, String cost){
		TangibleProduct t = new TangibleProduct(code,name,null,productCategory,cost==null?null:new BigDecimal(cost));
		t.setUseQuantity(new BigDecimal("1000"));
		t.setStockQuantity(new BigDecimal("1000"));
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
    
    private void bakeryFakeTransaction(){
    	ProductionPlanModel productionPlanModel = new ProductionPlanModel("PPM1","Production de pain");
    	productionPlanModel.setTimeDivisionType(RootBusinessLayer.getInstance().getTimeDivisionTypeDay());
    	productionPlanModel.getRows().add(new ProductionPlanModelInput(fakeTangibleProduct("F", "Farine", null, null)));
    	productionPlanModel.getRows().add(new ProductionPlanModelInput(fakeTangibleProduct("L", "Levure", null, null)));
    	productionPlanModel.getRows().add(new ProductionPlanModelInput(fakeTangibleProduct("A", "Ameliorant", null, null)));
    	
    	productionPlanModel.getColumns().add(new ProductionPlanModelMetric(inputName("PLANNED", "Planned Quantity")));
    	productionPlanModel.getColumns().add(new ProductionPlanModelMetric(inputName("FACTORED", "Factored Quantity")));
    	productionPlanModel.getColumns().add(new ProductionPlanModelMetric(inputName("UNUSED", "Unused Quantity")));
    	
    	productionPlanModelBusiness.create(productionPlanModel);
    	
		productionPlanModelBusiness.load(productionPlanModel);
    	Production production = new Production();
    	production.getPeriod().setFromDate(new Date());
    	production.getPeriod().setToDate(production.getPeriod().getFromDate());
    	for(ProductionPlanModelInput input : productionPlanModel.getRows()){
			for(ProductionPlanModelMetric productionPlanModelMetric : productionPlanModel.getColumns()){
				ProductionInput productionInput = new ProductionInput(input,productionPlanModelMetric);
				//productionInput.getMetricValue().setInput(productionPlanModelMetric.getInputName());
				production.getCells().add(productionInput);
				productionInput.setValue(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 9999)));
			}
		}
    	productionBusiness.create(production);
    }
    
}
