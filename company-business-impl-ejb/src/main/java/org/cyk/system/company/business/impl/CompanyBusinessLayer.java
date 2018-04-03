package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementBusinessImpl;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SalableProductCollectionPropertiesType;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.ClazzBusiness;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.AbstractFormatter;
import org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.business.impl.file.report.AbstractRootReportProducer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.ContentType;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.generator.StringGenerator;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.store.StoreType;
import org.cyk.system.root.model.time.Period;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.InstanceHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.cyk.utility.common.helper.TimeHelper;
import org.cyk.utility.common.test.TestCase;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	public static Class<? extends DataSet> DATA_SET_CLASS = RealDataSet.class;
	private static CompanyBusinessLayer INSTANCE;
	
	public static Boolean PRODUCT_STOCKING_ENABLED = Boolean.FALSE;
	public static Boolean AUTO_CREATE_CASHIER = Boolean.FALSE;
	
	private String pointOfSaleInvoiceReportName;
	private String pointOfSalePaymentReportName;
	private final String pointOfSaleReportExtension = "pdf";
	/*
	private String actionPrintEmployeeEmploymentContract = "print.employment.contract";
	private String actionPrintEmployeeWorkCertificate = "print.work.certificate";
	private String actionPrintEmployeeEmploymentCertificate = "print.employment.certificate";
	*/
	private String actionUpdateSalableProductInstanceCashRegisterState = "auspicrs";
	//private String action SalableProductInstanceCashRegisterState = "auspicrs";
	private final String actionCreateSaleCashRegisterMovementInput = "acscrmi";
	private final String actionCreateSaleCashRegisterMovementOutput = "acscrmo";
	
	public static final Integer PRODUCT_POINT_OF_SALE = 1000;
	public static final Integer PRODUCT_TANGIBLE_SALE_STOCK = 1001;
	public static final Integer PRODUCT_INTANGIBLE_SALE_STOCK = 1002;
	
	public static final Integer STRUCTURE_COMPANY = 2000;
	public static final Integer FILE_COMPANY_LOGO = 3000;
	public static final Integer ACCOUNTING_PERIOD = 4000;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		RootConstant.Code.setDefault(StoreType.class, RootConstant.Code.StoreType.PRODUCT);
		RootConstant.Code.setDefault(SalableProductCollectionPropertiesType.class, CompanyConstant.Code.SalableProductCollectionPropertiesType.SALE);
		//RootConstant.Code.setDefault(SalableProductCollectionProperties.class, CompanyConstant.Code.SalableProductCollectionProperties.SALE);
		
		PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT}, instanceCode)){
					if(PersistDataListener.BASE_PACKAGE.equals(name))
						return (T) CompanyBusinessLayer.class.getPackage();
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
		
		AbstractRootReportProducer.DEFAULT = new AbstractCompanyReportProducer.Default();
		formatterBusiness.registerFormatter(Production.class, new AbstractFormatter<Production>() {
			private static final long serialVersionUID = 3952155697329951912L;
			@Override
			public String format(Production production, ContentType contentType) {
				return inject(TimeBusiness.class).formatDate(production.getExistencePeriod().getFromDate());
			}
		});
		formatterBusiness.registerFormatter(AccountingPeriod.class, new AbstractFormatter<AccountingPeriod>() {
			private static final long serialVersionUID = 3952155697329951912L;
			@Override
			public String format(AccountingPeriod accountingPeriod, ContentType contentType) {
				return inject(TimeBusiness.class).formatPeriodFromTo(accountingPeriod.getExistencePeriod());
			}
		});
		formatterBusiness.registerFormatter(SalableProductInstance.class, new AbstractFormatter<SalableProductInstance>() {
			private static final long serialVersionUID = 3952155697329951912L;
			@Override
			public String format(SalableProductInstance salableProductInstance, ContentType contentType) {
				return salableProductInstance.getCode();
			}
		});
		/*formatterBusiness.registerFormatter(Cashier.class, new AbstractFormatter<Cashier>() {
			private static final long serialVersionUID = 3952155697329951912L;
			@Override
			public String format(Cashier cashier, ContentType contentType) {
				return cashier.get;
			}
		});*/
		
		
		pointOfSaleInvoiceReportName = inject(LanguageBusiness.class).findText("company.report.pointofsale.invoice");
		pointOfSalePaymentReportName = inject(LanguageBusiness.class).findText("company.report.pointofsale.paymentreceipt");
		
		ClazzBusiness.LISTENERS.add(new ClazzBusiness.ClazzBusinessListener.Adapter(){
			private static final long serialVersionUID = -6563167908087619179L;
			@Override
			public Object getParentOf(Object object) {
				if(object instanceof AbstractIdentifiable){
					AbstractIdentifiable identifiable = (AbstractIdentifiable) object;
					if(identifiable instanceof AccountingPeriod)
						return null;
					if(identifiable instanceof Sale)
						return null;//((Sale)identifiable).getAccountingPeriod();
					
					if(identifiable instanceof SalableProductCollectionItem)
						return ((SalableProductCollectionItem)identifiable).getSalableProduct();
					
					if(identifiable instanceof SaleCashRegisterMovement)
						return ((SaleCashRegisterMovement)identifiable).getSale();
					if(identifiable instanceof SalableProductCollectionItemSaleCashRegisterMovement)
						return ((SalableProductCollectionItemSaleCashRegisterMovement)identifiable).getSaleCashRegisterMovement();
					
				}
				return super.getParentOf(object);
			}
		});
		
		//TODO I do not know how to handle sale
		
		inject(GlobalIdentifierPersistenceMappingConfigurations.class).configure();
		
		/*InstanceHelper.getInstance().setFieldValueGenerator(ProductStore.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE), new InstanceHelper.Listener.FieldValueGenerator.Adapter.Default<String>(String.class){
			private static final long serialVersionUID = 1L;
			@Override
			protected String __execute__(Object instance, String fieldName,Class<String> outputClass) {
				return "PS"+System.currentTimeMillis()+RandomHelper.getInstance().getAlphabetic(4);
			}
		});*/
		
		InstanceHelper.getInstance().setFieldValueGenerator(Sale.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE), new InstanceHelper.Listener.FieldValueGenerator.Adapter.Default<String>(String.class){
			private static final long serialVersionUID = 1L;
			@Override
			protected String __execute__(Object instance, String fieldName,Class<String> outputClass) {
				return "SALE"+System.currentTimeMillis()+RandomHelper.getInstance().getAlphabetic(4);
			}
		});
		
		InstanceHelper.getInstance().setFieldValueGenerator(Sale.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_EXISTENCE_PERIOD,Period.FIELD_FROM_DATE), new InstanceHelper.Listener.FieldValueGenerator.Adapter.Default<Date>(Date.class){
			private static final long serialVersionUID = 1L;
			@Override
			protected Date __execute__(Object instance, String fieldName,Class<Date> outputClass) {
				return TimeHelper.getInstance().getUniversalTimeCoordinated();
			}
		});
		
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{"code","name"}, SalableProductCollectionItem.class,ProductStore.class);
		
		ClassHelper.getInstance().map(TestCase.class, CompanyBusinessTestHelper.TestCase.class);
	}
		
	@Override
	protected AbstractReportRepository getReportRepository() {
		return inject(CompanyReportRepository.class);
	}
	
	@Override
	protected void persistStructureData() {
		super.persistStructureData();
		if(DATA_SET_CLASS==null)
    		;
    	else{
    		DataSet dataSet = ClassHelper.getInstance().instanciateOne(DATA_SET_CLASS);
        	dataSet.instanciate().save();	
    	}		
	}
	
	/*@Override
	protected void persistSecurityData() {
		super.persistSecurityData();
		security(dataSet);
	}*/
		
	/**/
	
	@Override
	protected void setConstants(){}
	
	public StockableProduct getStockableProductStocking(){
		return null;
	}

	public static CompanyBusinessLayer getInstance() {
		return INSTANCE;
	}
		
	public void enableEnterpriseResourcePlanning(){
		EmployeeBusinessImpl.Listener.COLLECTION.add(EMPLOYEE_BUSINESS_LISTENER); 
		CashRegisterMovementBusinessImpl.Listener.COLLECTION.add(CASH_REGISTER_MOVEMENT_BUSINESS_LISTENER);
		SalableProductCollectionBusinessImpl.Listener.COLLECTION.add(SALABLE_PRODUCT_COLLECTION_BUSINESS_LISTENER);
		
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{GlobalIdentifier.FIELD_CODE}, SaleCashRegisterMovement.class
				,SaleCashRegisterMovementCollection.class);
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{
				commonUtils.attributePath(GlobalIdentifier.FIELD_EXISTENCE_PERIOD,Period.FIELD_FROM_DATE)}, CashRegisterMovement.class,Sale.class
				,SaleCashRegisterMovementCollection.class,SaleCashRegisterMovement.class);
		
		CompanyConstant.Configuration.Sale.AUTOMATICALLY_GENERATE_REPORT_FILE = Boolean.TRUE;
		CompanyConstant.Configuration.SaleCashRegisterMovementCollection.AUTOMATICALLY_GENERATE_REPORT_FILE = Boolean.TRUE;
	}
	
	/**/
	
	public static EmployeeBusinessImpl.Listener EMPLOYEE_BUSINESS_LISTENER 
		= new EmployeeBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning();
	public static SalableProductCollectionBusinessImpl.Listener SALABLE_PRODUCT_COLLECTION_BUSINESS_LISTENER 
	= new SalableProductCollectionBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning();
	public static SaleBusinessImpl.Listener SALE_BUSINESS_LISTENER 
		= new SaleBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning();
	public static CashRegisterMovementBusinessImpl.Listener CASH_REGISTER_MOVEMENT_BUSINESS_LISTENER 
		= new CashRegisterMovementBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning();
	/**/
	
	public static interface Listener extends AbstractBusinessLayer.Listener {
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		void handleCompanyToInstall(Company company);
		void handleCompanyLogoToInstall(File file);
		void handlePointOfSaleToInstall(File file);
		void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod);
		
		String getCompanyName();
		
		/* File */
		
		byte[] getCompanyLogoBytes();
		byte[] getCompanyPointOfSaleBytes();
		
		/* Identifier */

		StringGenerator getSaleIdentifierGenerator();
		StringGenerator getCashRegisterMovementIdentifierGenerator();
		
		String getDocumentHeaderFileRelativePath();
		String getDocumentBackgroundFileRelativePath();
		
		/**/
		/*
		Collection<Employee> getEmployees();
		Employee getManager();
		
		UserAccount getUserAccount(Person person);
		*/
		/**/
		
		//String SALE_IDENTIFIER = "SALE_IDENTIFIER";
		//String CASH_MOVEMENT_IDENTIFIER = "CASH_MOVEMENT_IDENTIFIER";
		
		/**/
		
		public static class Adapter extends AbstractBusinessLayer.Listener.Adapter implements Listener {

			private static final long serialVersionUID = -3717816726680012239L;

			@Override
			public void handleCompanyToInstall(Company company) {}

			@Override
			public void handleCompanyLogoToInstall(File file) {}

			@Override
			public void handlePointOfSaleToInstall(File file) {}

			@Override
			public void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod) {}

			@Override
			public String getCompanyName() {
				return null;
			}

			@Override
			public byte[] getCompanyLogoBytes() {
				return null;
			}

			@Override
			public byte[] getCompanyPointOfSaleBytes() {
				return null;
			}

			@Override
			public StringGenerator getSaleIdentifierGenerator() {
				return null;
			}

			@Override
			public StringGenerator getCashRegisterMovementIdentifierGenerator() {
				return null;
			}
			
			@Override
			public String getDocumentBackgroundFileRelativePath() {
				return null;
			}
			@Override
			public String getDocumentHeaderFileRelativePath() {
				return null;
			}

		}

	}
}
