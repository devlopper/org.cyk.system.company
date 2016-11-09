package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.accounting.AccountingPeriodBusinessImpl;
import org.cyk.system.company.business.impl.accounting.AccountingPeriodProductBusinessImpl;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementBusinessImpl;
import org.cyk.system.company.business.impl.sale.CustomerBusinessImpl;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.stock.StockTangibleProductMovementBusinessImpl;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.AbstractFormatter;
import org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl;
import org.cyk.system.root.business.impl.BusinessServiceProvider;
import org.cyk.system.root.business.impl.BusinessServiceProvider.Service;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.business.impl.file.report.AbstractRootReportProducer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;
import org.cyk.system.root.model.ContentType;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.generator.StringGenerator;
import org.cyk.system.root.model.geography.ContactCollection;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.BusinessServiceCollection;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.computation.DataReadConfiguration;
import org.joda.time.DateTime;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

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
		
		ApplicationBusinessImpl.Listener.COLLECTION.add(new ApplicationBusinessImpl.Listener.Adapter.Default(){
			private static final long serialVersionUID = 5234235361543643487L;
			@Override
			public void installationEnded(Installation installation) {
				super.installationEnded(installation);
				OwnedCompany ownedCompany = inject(OwnedCompanyBusiness.class).findDefaultOwnedCompany();
				Collection<Person> persons = inject(PersonDao.class).select().all();
				ownedCompany.getCompany().setManager(persons.isEmpty() ? null : persons.iterator().next());
				inject(CompanyBusiness.class).update(ownedCompany.getCompany());
				if(Boolean.TRUE.equals(AUTO_CREATE_CASHIER)){
					CashRegister cashRegister = create(new CashRegister("CashRegister01",ownedCompany,createMovementCollection("CashRegisterMovementCollection01", "Entrée", "Sortie")));
					inject(CashierBusiness.class).create(new Cashier(ownedCompany.getCompany().getManager(),cashRegister));
				}
			}
		});
		
		BusinessServiceProvider.Identifiable.COLLECTION.add(new AbstractActorBusinessImpl.BusinessServiceProviderIdentifiable<Customer,Customer.SearchCriteria>(Customer.class){
			private static final long serialVersionUID = 1322416788278558869L;
			
			@Override
			protected Customer.SearchCriteria createSearchCriteria(Service service,DataReadConfiguration dataReadConfiguration) {
				return new Customer.SearchCriteria(dataReadConfiguration.getGlobalFilter());
			}
        });
		
		//TODO I do not know how to handle sale
		SaleBusinessImpl.Listener.COLLECTION.add(new StockTangibleProductMovementBusinessImpl.SaleBusinessAdapter());
		SaleBusinessImpl.Listener.COLLECTION.add(new AccountingPeriodBusinessImpl.SaleBusinessAdapter());
		SaleBusinessImpl.Listener.COLLECTION.add(new AccountingPeriodProductBusinessImpl.SaleBusinessAdapter());
		SaleBusinessImpl.Listener.COLLECTION.add(new CustomerBusinessImpl.SaleBusinessAdapter());
	
	}
		
	@Override
	protected AbstractReportRepository getReportRepository() {
		return inject(CompanyReportRepository.class);
	}
	
	/*@Override
	protected RootReportProducer getReportProducer() {
		return inject(CompanyReportProducer.class);
	}*/
	
	@Override
	protected void persistData() {
		security();
		structure();
		company();
		
		if(Boolean.TRUE.equals(PRODUCT_STOCKING_ENABLED)){
			create(new IntangibleProduct(IntangibleProduct.STOCKING, IntangibleProduct.STOCKING, null));
	    	create(new TangibleProduct(TangibleProduct.STOCKING, TangibleProduct.STOCKING, null));
	    	
	    	create(new SalableProduct(getEnumeration(IntangibleProduct.class, IntangibleProduct.STOCKING), null));
	    	create(new StockableTangibleProduct(getEnumeration(TangibleProduct.class, TangibleProduct.STOCKING)
	    			, rootDataProducerHelper.createMovementCollection(TangibleProduct.STOCKING, "Input", "Output")));	
		}
		
		createEnumeration(CashRegisterMovementMode.class, CashRegisterMovementMode.CASH);
		CashRegisterMovementMode cashRegisterMovementMode = new CashRegisterMovementMode(CashRegisterMovementMode.CHEQUE, CashRegisterMovementMode.CHEQUE, null);
		cashRegisterMovementMode.setSupportDocumentIdentifier(Boolean.TRUE);
		create(cashRegisterMovementMode);
		
		cashRegisterMovementMode = new CashRegisterMovementMode(CashRegisterMovementMode.GIFT_CARD, CashRegisterMovementMode.GIFT_CARD, null);
		cashRegisterMovementMode.setSupportDocumentIdentifier(Boolean.TRUE);
		create(cashRegisterMovementMode);

	}
	
	private void company(){ 
		byte[] bytes = null;
		
		Company company = new Company();
		company.setCode("C01");
		String companyName = null;
		for(Listener listener : Listener.COLLECTION){
			String value = listener.getCompanyName();
			if(value!=null)
				companyName = value;
		}
		company.setName(companyName==null?"MyCompany":companyName);
		bytes = null;
		for(Listener listener : Listener.COLLECTION){
			byte[] value = listener.getCompanyLogoBytes();
			if(value!=null)
				bytes = value;
		}
		company.setImage(inject(FileBusiness.class).process(bytes==null?getResourceAsBytes("image/logo.png"):bytes,"companylogo.png"));
		//fileBusiness.create(company.getImage());
		for(Listener listener : Listener.COLLECTION)
			listener.handleCompanyLogoToInstall(company.getImage());
		installObject(FILE_COMPANY_LOGO,inject(FileBusiness.class),company.getImage());
		company.setContactCollection(new ContactCollection());
		//company.getContactCollection().setPhoneNumbers(new ArrayList<PhoneNumber>());
		//RootRandomDataProvider.getInstance().phoneNumber(company.getContactCollection());
		
		for(Listener listener : Listener.COLLECTION)
			listener.handleCompanyToInstall(company);
		installObject(STRUCTURE_COMPANY,inject(CompanyBusiness.class),company);
		
		OwnedCompany ownedCompany = new OwnedCompany();
		ownedCompany.setCompany(company);
		ownedCompany.setSelected(Boolean.TRUE);
		//ownedCompanyBusiness.create(ownedCompany);
		installObject(-1,inject(OwnedCompanyBusiness.class),ownedCompany);
		
		/*FiniteStateMachine finiteStateMachine = rootDataProducerHelper.createFiniteStateMachine("SALE_FINITE_MACHINE_STATE"
    			, new String[]{"SALE_FINITE_MACHINE_ALPHABET_VALID"}
    		, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
    		, Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}, new String[][]{
    			{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,"SALE_FINITE_MACHINE_ALPHABET_VALID",Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
    	});*/
		
		FiniteStateMachine salableProductInstanceCashRegisterFiniteStateMachine = rootDataProducerHelper.createFiniteStateMachine(CompanyConstant.GIFT_CARD_WORKFLOW
    			, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SEND,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE}
				, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD
				,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}
    			,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}, new String[][]{
    			{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SEND,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}
    	});
		updateEnumeration(FiniteStateMachineAlphabet.class, CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SEND, "Transférer");
		updateEnumeration(FiniteStateMachineAlphabet.class, CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE, "Réceptionner");
		updateEnumeration(FiniteStateMachineAlphabet.class, CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL, "Vendre");
		updateEnumeration(FiniteStateMachineAlphabet.class, CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE, "Utiliser");
		
		updateEnumeration(FiniteStateMachineState.class, CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED, "Assigné");
		updateEnumeration(FiniteStateMachineState.class, CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT, "Transféré");
		updateEnumeration(FiniteStateMachineState.class, CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED, "Réceptionné");
		updateEnumeration(FiniteStateMachineState.class, CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD, "Vendu");
		updateEnumeration(FiniteStateMachineState.class, CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED, "Utilisé");
		
		createReportTemplate(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT,"contrat de travail",Boolean.TRUE, "report/employee/employment_contract.jrxml", null, null, null);
		createReportTemplate(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CERTIFICATE,"certificat d'emploi",Boolean.TRUE, "report/employee/employment_certificate.jrxml", null, null, null);
		createReportTemplate(CompanyConstant.REPORT_EMPLOYEE_WORK_CERTIFICATE,"certificat de travail",Boolean.TRUE, "report/employee/work_certificate.jrxml", null, null, null);
		createReportTemplate(CompanyConstant.REPORT_INVOICE,"facture",Boolean.FALSE, "report/sale/invoice_a4.jrxml", null, null, null);
		createReportTemplate(CompanyConstant.REPORT_PAYMENT_RECEIPT,"reçu de paiement",Boolean.TRUE, "report/sale/payment_receipt_a4.jrxml", null, null, null);
		
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setOwnedCompany(ownedCompany);
		Integer currentYear = new DateTime().getYear();
		accountingPeriod.setExistencePeriod(new Period(new DateTime(currentYear, 1, 1, 0, 0).toDate(), new DateTime(currentYear, 12, 31, 23, 59).toDate()));
		
		accountingPeriod.getSaleConfiguration().setValueAddedTaxRate(BigDecimal.ZERO);
		accountingPeriod.getSaleConfiguration().setIdentifierGenerator(stringGenerator("FACT","0", 8l, null, null,8l));
		accountingPeriod.getSaleConfiguration().setCashRegisterMovementIdentifierGenerator(stringGenerator("PAIE","0", 8l, null, null,8l));
		//accountingPeriod.getSaleConfiguration().setFiniteStateMachine(finiteStateMachine);
		accountingPeriod.getSaleConfiguration().setSalableProductInstanceCashRegisterFiniteStateMachine(salableProductInstanceCashRegisterFiniteStateMachine);
		accountingPeriod.getSaleConfiguration().setSalableProductInstanceCashRegisterSaleConsumeState(inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD));
		
		inject(StringGeneratorBusiness.class).create(accountingPeriod.getSaleConfiguration().getIdentifierGenerator());
		inject(StringGeneratorBusiness.class).create(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator());
		//accountingPeriodBusiness.create(accountingPeriod);
		for(Listener listener : Listener.COLLECTION)
			listener.handleAccountingPeriodToInstall(accountingPeriod);
		installObject(ACCOUNTING_PERIOD,inject(AccountingPeriodBusiness.class),accountingPeriod);
		
		//intangibleProductBusiness.create(new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null, null));
		//installObject(PRODUCT_INTANGIBLE_SALE_STOCK,intangibleProductBusiness,new IntangibleProduct(IntangibleProduct.SALE_STOCK, "Stockage de marchandise", null, null));
		
		//tangibleProductBusiness.create(new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null, null));
		//installObject(PRODUCT_TANGIBLE_SALE_STOCK,tangibleProductBusiness,new TangibleProduct(TangibleProduct.SALE_STOCK, "Marchandise", null, null));
		
		createEnumeration(EmploymentAgreementType.class,EmploymentAgreementType.CDD);
		createEnumeration(EmploymentAgreementType.class,EmploymentAgreementType.CDI);	
		
		
		createFile(CompanyConstant.FILE_DOCUMENT_HEADER,null);
    	createFile(CompanyConstant.FILE_DOCUMENT_BACKGROUND,null);
    	createFile(CompanyConstant.FILE_DOCUMENT_BACKGROUND_DRAFT,null);
	}
	
	private void security(){ 
		for(String code : new String[]{CompanyConstant.BUSINESS_SERVICE_COLLECTION_ACCOUNTING,CompanyConstant.BUSINESS_SERVICE_COLLECTION_COMPANY
				,CompanyConstant.BUSINESS_SERVICE_COLLECTION_PAYMENT,CompanyConstant.BUSINESS_SERVICE_COLLECTION_PRODUCT,CompanyConstant.BUSINESS_SERVICE_COLLECTION_PRODUCTION
				,CompanyConstant.BUSINESS_SERVICE_COLLECTION_SALE,CompanyConstant.BUSINESS_SERVICE_COLLECTION_STOCK})
        	createEnumeration(BusinessServiceCollection.class,code);
	}
		
	private void structure(){
		DivisionType department = new DivisionType(null, DivisionType.DEPARTMENT, "Department");
        create(department);
    }
	
	/**/
	
	@Override
	protected void setConstants(){}
	
	public IntangibleProduct getIntangibleProductStocking(){
		return getEnumeration(IntangibleProduct.class,IntangibleProduct.STOCKING);
	}
	public TangibleProduct getTangibleProductStocking(){
		return getEnumeration(TangibleProduct.class,TangibleProduct.STOCKING);
	}
	public SalableProduct getSalableProductStocking(){
		return inject(SalableProductDao.class).readByProduct(getIntangibleProductStocking());
	}
	public StockableTangibleProduct getStockableTangibleProductStocking(){
		return inject(StockableTangibleProductDao.class).readByTangibleProduct(getTangibleProductStocking());
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
		
	/**/
	
	protected void fakeTransactions(){}
	
	/**/
	
	public void enableEnterpriseResourcePlanning(){
		EmployeeBusinessImpl.Listener.COLLECTION.add(EMPLOYEE_BUSINESS_LISTENER); 
		CashRegisterMovementBusinessImpl.Listener.COLLECTION.add(CASH_REGISTER_MOVEMENT_BUSINESS_LISTENER);
		SalableProductCollectionBusinessImpl.Listener.COLLECTION.add(SALABLE_PRODUCT_COLLECTION_BUSINESS_LISTENER);
		SaleBusinessImpl.Listener.COLLECTION.add(SALE_BUSINESS_LISTENER);
		
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{GlobalIdentifier.FIELD_CODE}, SalableProductCollectionItem.class
				,SaleCashRegisterMovement.class);
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{
				commonUtils.attributePath(GlobalIdentifier.FIELD_EXISTENCE_PERIOD,Period.FIELD_FROM_DATE)}, CashRegisterMovement.class,Sale.class
				,SaleCashRegisterMovement.class);
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
