package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollectionReportTemplateFile;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReportFile;
import org.cyk.system.company.model.sale.SaleReportTemplateFile;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmployeeReportTemplateFile;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemSaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.party.person.PersonBusiness;
import org.cyk.system.root.business.api.party.person.PersonRelationshipBusiness;
import org.cyk.system.root.business.api.party.person.PersonRelationshipTypeRoleBusiness;
import org.cyk.system.root.business.impl.file.report.AbstractRootReportProducer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.system.root.model.party.Application;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.party.person.PersonRelationship;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.ListenerUtils;

public abstract class AbstractCompanyReportProducer extends AbstractRootReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;
	
	@Override
	public Class<?> getReportTemplateFileClass(AbstractIdentifiable identifiable, String reportTemplateCode) {
		if(identifiable instanceof Employee){
			if(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT.equals(reportTemplateCode))
				return EmployeeReportTemplateFile.class;
			if(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE.equals(reportTemplateCode))
				return EmployeeReportTemplateFile.class;
			if(CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE.equals(reportTemplateCode))
				return EmployeeReportTemplateFile.class;
		}
		
		if(identifiable instanceof Sale){
			if(CompanyConstant.Code.ReportTemplate.INVOICE.equals(reportTemplateCode))
				return SaleReportTemplateFile.class;
			if(CompanyConstant.Code.ReportTemplate.INVOICE_AND_PAYMENT_RECEIPT.equals(reportTemplateCode))
				return SaleReportTemplateFile.class;
		}
		
		if(identifiable instanceof SaleCashRegisterMovementCollection){
			if(CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4.equals(reportTemplateCode))
				return SaleCashRegisterMovementCollectionReportTemplateFile.class;
		}
		
		if(identifiable instanceof SaleCashRegisterMovement){
			if(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT.equals(reportTemplateCode))
				return SaleCashRegisterMovementReportFile.class;
		}
		
		return super.getReportTemplateFileClass(identifiable, reportTemplateCode);
	}
	
	private SaleReportTemplateFile produceSaleReportTemplateFile(Sale sale) {
		sale.getSalableProductCollection().getItems().setElements(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
		inject(SalableProductCollectionBusiness.class).computeDerivationsFromCost(sale.getSalableProductCollection());
		SaleReportTemplateFile report = new SaleReportTemplateFile(sale);
		
		report.addLabelValues("Invoice",new String[][]{
			{"Date",Constant.EMPTY_STRING}
			,{report.getSale().getBirthDate(),Constant.EMPTY_STRING}
			,{"Invoice No.",Constant.EMPTY_STRING}
			,{report.getSale().getCode(),Constant.EMPTY_STRING}
			,{"Commercial Name",Constant.EMPTY_STRING}
			,{ sale.getGlobalIdentifier().getOwner() instanceof Application ? sale.getGlobalIdentifier().getOwner().getName() 
					: ((Person)sale.getGlobalIdentifier().getOwner()).getNames(),Constant.EMPTY_STRING}
			,{"Parent",Constant.EMPTY_STRING}
			,{sale.getCustomer()==null ? "CUST???" : sale.getCustomer().getPerson().getNames(),Constant.EMPTY_STRING}			
		});
		
		//for(SalableProductCollectionItem item : inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()))
		//	report.getSale().getSalableProductCollection().getItems().add(new SalableProductCollectionItemReport(report.getSale().getSalableProductCollection(),item));
		
		//set(sale, saleReport);
		//report.setSale(saleReport);
		/*
		report.generate();
		report.addLabelValueCollection("Invoice",new String[][]{
				{"Identifiant", report.getSale().getGlobalIdentifier().getIdentifier()}
				,{"Caisse", report.getSale().getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getCashRegister().getGlobalIdentifier().getCode()}
				,{"Date", report.getSale().getGlobalIdentifier().getExistencePeriod().getFrom()}
				,{"Client", report.getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		report.addLabelValueCollection("Payment",new String[][]{
				{"A payer", report.getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		report.addLabelValueCollection("TVA",new String[][]{
				{"Taux TVA", report.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSale().getSalableProductCollection().getCost().getTax()}
				});
		*/
		return report;
	}
	
	private SaleCashRegisterMovementCollectionReportTemplateFile produceSaleCashRegisterMovementCollectionReportTemplateFile(final SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
		saleCashRegisterMovementCollection.getItems().setElements(inject(SaleCashRegisterMovementDao.class).readByCollection(saleCashRegisterMovementCollection));
		SaleCashRegisterMovementCollectionReportTemplateFile report = new SaleCashRegisterMovementCollectionReportTemplateFile(saleCashRegisterMovementCollection);
		//report.getSaleCashRegisterMovementCollection().getCashRegisterMovement().setText("DETAILS TO COMPUTE");
		//List<SaleCashRegisterMovement> saleCashRegisterMovements = (List<SaleCashRegisterMovement>) inject(SaleCashRegisterMovementDao.class).readByCollection(saleCashRegisterMovementCollection);
		
		/* Previous */
		for(SaleCashRegisterMovementReport index : report.getSaleCashRegisterMovementCollection().getSaleCashRegisterMovements()){
			List<SaleCashRegisterMovement> collection = (List<SaleCashRegisterMovement>) inject(SaleCashRegisterMovementDao.class)
					.readBySale( ((SaleCashRegisterMovement) index.getSource()).getSale() );
			if(collection.size()>1)
				index.setPrevious(new SaleCashRegisterMovementReport(report.getSaleCashRegisterMovementCollection(),collection.get(collection.size()-2)));
		}
		
		report.setTitle(languageBusiness.findText("company.report.sale"));
		report.setFooter(languageBusiness.findText("company.report.pointofsale.welcome"));
		report.setHeader(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		Collection<Person> customerPersons = listenerUtils.getCollection(Listener.COLLECTION, new ListenerUtils.CollectionMethod<Listener, Person>(){
			@Override
			public Collection<Person> execute(Listener listener) {
				return listener.getCustomerPersons(saleCashRegisterMovementCollection);
			}
			
			@Override
			public Collection<Person> getNullValue() {
				Collection<Person> persons = new LinkedHashSet<>();
				for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovementCollection.getItems().getElements())
					if(saleCashRegisterMovement.getSale().getCustomer()!=null)
						persons.add(saleCashRegisterMovement.getSale().getCustomer().getPerson());
				return persons;
			}
		});
		if(customerPersons==null){
			customerPersons = new LinkedHashSet<>();
			for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovementCollection.getItems().getElements()){
				if(saleCashRegisterMovement.getSale().getCustomer()!=null)
					customerPersons.add(saleCashRegisterMovement.getSale().getCustomer().getPerson());
			}
		}
		
		Set<String> customerNames = new LinkedHashSet<>();
		for(Person person : customerPersons){
			customerNames.add(inject(PersonBusiness.class).findNames(person));
		}
		
		String customerLabel = listenerUtils.getString(Listener.COLLECTION, new ListenerUtils.StringMethod<Listener>() {

			@Override
			public String execute(Listener listener) {
				return listener.getCustomerLabel(saleCashRegisterMovementCollection);
			}
			
			@Override
			public String getNullValue() {
				return inject(LanguageBusiness.class).findClassLabelText(Customer.class);
			}
		});
		
		report.addLabelValues("Payment",new String[][]{
			{"Date",Constant.EMPTY_STRING}
			,{report.getSaleCashRegisterMovementCollection().getCreationDate(),Constant.EMPTY_STRING}
			,{"Receipt No.",Constant.EMPTY_STRING}
			,{report.getSaleCashRegisterMovementCollection().getCode(),Constant.EMPTY_STRING}
			,{"Cashier Name",Constant.EMPTY_STRING}
			,{ saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner() instanceof Application ? saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner().getName() 
					: ((Person)saleCashRegisterMovementCollection.getGlobalIdentifier().getOwner()).getNames(),Constant.EMPTY_STRING}
			,{customerLabel,Constant.EMPTY_STRING}
			,{StringUtils.join(customerNames,Constant.CHARACTER_COMA.toString()),Constant.EMPTY_STRING}	
			,{"Received from",Constant.EMPTY_STRING}
			,{saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().getSenderOrReceiverPersonAsString(),Constant.EMPTY_STRING}	
		});
		report.getCurrentLabelValueCollection().setIdentifier(SaleCashRegisterMovementCollectionReportTemplateFile.LABEL_VALUES_PAYMENT);
		Listener.Adapter.processLabelValueCollection(Listener.COLLECTION, report.getSaleCashRegisterMovementCollection(), report.getCurrentLabelValueCollection());
		Listener.Adapter.process(Listener.COLLECTION, report);
		
		return report;
	}
	
	private SaleCashRegisterMovementReportFile produceSaleCashRegisterMovementReportFile(SaleCashRegisterMovement saleCashRegisterMovement) {
		SaleCashRegisterMovementReportFile report = new SaleCashRegisterMovementReportFile(saleCashRegisterMovement);
		saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().getElements().clear();
		//List<SaleCashRegisterMovement> saleCashRegisterMovements = (List<SaleCashRegisterMovement>) inject(SaleCashRegisterMovementDao.class).readBySale(saleCashRegisterMovement.getSale());
		/*if(saleCashRegisterMovements.size()>1){
			report.getSaleCashRegisterMovement().setPrevious(new SaleCashRegisterMovementReport(report.getSaleCashRegisterMovement().getSale()
					, saleCashRegisterMovements.get(saleCashRegisterMovements.size()-2)));
			
		}*/
		
		/* Previous */
		for(SalableProductCollectionItemSaleCashRegisterMovementReport index : report.getSaleCashRegisterMovement().getSalableProductCollectionItemSaleCashRegisterMovements()){
			SalableProductCollectionItem salableProductCollectionItem = (SalableProductCollectionItem) index.getSalableProductCollectionItem().getSource();
			List<SalableProductCollectionItemSaleCashRegisterMovement> collection = (List<SalableProductCollectionItemSaleCashRegisterMovement>) inject(SalableProductCollectionItemSaleCashRegisterMovementDao.class)
					.readBySalableProductCollectionItem(salableProductCollectionItem);
			if(collection.size()>1)
				index.setPrevious(new SalableProductCollectionItemSaleCashRegisterMovementReport(index.getSalableProductCollectionItem(), index.getSaleCashRegisterMovement()
						,collection.get(collection.size()-2)));
		}
		
		report.setTitle(languageBusiness.findText("company.report.sale"));
		report.setFooter(languageBusiness.findText("company.report.pointofsale.welcome"));
		report.setHeader(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		report.addLabelValues("Payment",new String[][]{
			{"Date",Constant.EMPTY_STRING}
			,{report.getSaleCashRegisterMovement().getBirthDate(),Constant.EMPTY_STRING}
			,{"Receipt No.",Constant.EMPTY_STRING}
			,{report.getSaleCashRegisterMovement().getCode(),Constant.EMPTY_STRING}
			,{"Cashier Name",Constant.EMPTY_STRING}
			,{ saleCashRegisterMovement.getGlobalIdentifier().getOwner() instanceof Application ? saleCashRegisterMovement.getGlobalIdentifier().getOwner().getName() 
					: ((Person)saleCashRegisterMovement.getGlobalIdentifier().getOwner()).getNames(),Constant.EMPTY_STRING}
			,{"Parent",Constant.EMPTY_STRING}
			,{saleCashRegisterMovement.getSale().getCustomer().getPerson().getNames(),Constant.EMPTY_STRING}	
			,{"Received from",Constant.EMPTY_STRING}
			,{saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().getSenderOrReceiverPerson()==null
					? saleCashRegisterMovement.getSale().getCustomer().getPerson().getNames() 
						: saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().getSenderOrReceiverPerson().getNames(),Constant.EMPTY_STRING}	
		});
		
		/*
		report.addLabelValues("MyPayment",new String[][]{
				{"Identifiant", report.getSaleCashRegisterMovement().getGlobalIdentifier().getIdentifier()}
				,{"Caisse", report.getSaleCashRegisterMovement().getCashRegisterMovement().getCashRegister().getGlobalIdentifier().getCode()}
				,{"Date", report.getSaleCashRegisterMovement().getGlobalIdentifier().getExistencePeriod().getFrom()}
				//,{"Client", report.getSaleCashRegisterMovement().getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		report.addLabelValues("Payment",new String[][]{
				{"A payer", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		report.addLabelValues("TVA",new String[][]{
				{"Taux TVA", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getTax()}
				});
				*/
		
		return report;
	}
	
	private EmployeeReportTemplateFile produceEmployeeReportTemplateFile(Employee employee) {
		EmployeeReportTemplateFile report = new EmployeeReportTemplateFile();
		set(employee, report.getActor());
		return report;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <REPORT extends AbstractReportTemplateFile<REPORT>> REPORT produce(Class<REPORT> reportTemplateFileClass,CreateReportFileArguments<?> createReportFileArguments) {
		if(SaleReportTemplateFile.class.equals(reportTemplateFileClass)){
			if(createReportFileArguments.getIdentifiable() instanceof Sale)
				return (REPORT) produceSaleReportTemplateFile((Sale)createReportFileArguments.getIdentifiable());
		}else if(SaleCashRegisterMovementCollectionReportTemplateFile.class.equals(reportTemplateFileClass)){
			if(createReportFileArguments.getIdentifiable() instanceof SaleCashRegisterMovementCollection)
				return (REPORT) produceSaleCashRegisterMovementCollectionReportTemplateFile((SaleCashRegisterMovementCollection)createReportFileArguments.getIdentifiable());
		}else if(SaleCashRegisterMovementReportFile.class.equals(reportTemplateFileClass)){
			if(createReportFileArguments.getIdentifiable() instanceof SaleCashRegisterMovement)
				return (REPORT) produceSaleCashRegisterMovementReportFile((SaleCashRegisterMovement)createReportFileArguments.getIdentifiable());
		}else if(EmployeeReportTemplateFile.class.equals(reportTemplateFileClass)){
			if(createReportFileArguments.getIdentifiable() instanceof Employee)
				return (REPORT) produceEmployeeReportTemplateFile((Employee)createReportFileArguments.getIdentifiable());
		}
		
		return super.produce(reportTemplateFileClass, createReportFileArguments);
	}
		
	/**/
	
	public static interface Listener extends AbstractRootReportProducer.Listener {
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		Person process(Person person);
		Collection<Person> getCustomerPersons(AbstractIdentifiable identifiable);
		Person getCustomerPerson(AbstractIdentifiable identifiable);
		String getCustomerLabel(AbstractIdentifiable identifiable);
		String[] getCustomerPersonRelationshipTypeRoleCodes(AbstractIdentifiable identifiable);
		
		/**/
		
		public static class Adapter extends AbstractRootReportProducer.Listener.Adapter implements Listener,Serializable {
			private static final long serialVersionUID = 1L;
			
			/**/
			
			@Override
			public Collection<Person> getCustomerPersons(AbstractIdentifiable identifiable) {
				return null;
			}
			
			@Override
			public Person getCustomerPerson(AbstractIdentifiable identifiable) {
				return null;
			}

			@Override
			public String getCustomerLabel(AbstractIdentifiable identifiable) {
				return null;
			}
			
			@Override
			public String[] getCustomerPersonRelationshipTypeRoleCodes(AbstractIdentifiable identifiable) {
				return null;
			}
			
			@Override
			public Person process(Person person) {
				return null;
			}
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = 1L;
				
				@Override
				public Collection<Person> getCustomerPersons(AbstractIdentifiable identifiable) {
					Collection<Person> persons = new ArrayList<>();
					if(identifiable instanceof SaleCashRegisterMovementCollection)
						for(SaleCashRegisterMovement saleCashRegisterMovement : ((SaleCashRegisterMovementCollection)identifiable).getItems().getElements()){
							Person person = getCustomerPerson(saleCashRegisterMovement.getSale().getCustomer());
							if(person!=null)
								person = process(person);
							
							if(person!=null)
								persons.add(person);
							
						}
					return persons;
				}
				
				@Override
				public Person getCustomerPerson(AbstractIdentifiable identifiable) {
					if(identifiable instanceof Customer)
						return((Customer)identifiable).getPerson();
					return null;
				}
				
				@Override
				public Person process(Person person) {
					String[] personRelationshipTypeRoleCodes = getCustomerPersonRelationshipTypeRoleCodes(person);
					if(personRelationshipTypeRoleCodes!=null && personRelationshipTypeRoleCodes.length>0){
						Collection<PersonRelationship> personRelationships = inject(PersonRelationshipBusiness.class).findOppositeByPersonByRoles(person, inject(PersonRelationshipTypeRoleBusiness.class)
								.find(Arrays.asList(personRelationshipTypeRoleCodes)));
						Collection<Person> relatedPersons = inject(PersonRelationshipBusiness.class).getRelatedPersons(personRelationships, person);
						Person related = relatedPersons.isEmpty() ? null : relatedPersons.iterator().next();
						if(related!=null)
							person = related;
					}
					return person;
				}
				
			}			
			
		}
	}
	
	/**/
	
	public static class Default extends AbstractCompanyReportProducer implements Serializable {
		private static final long serialVersionUID = 1L;
		
	}
}
