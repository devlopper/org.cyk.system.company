package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

import org.apache.commons.io.IOUtils;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.Report;
import org.cyk.system.root.model.file.report.ReportConfiguration;
import org.cyk.system.root.model.geography.Locality;
import org.cyk.system.root.model.geography.LocalityType;
import org.cyk.system.root.model.geography.PhoneNumber;
import org.cyk.system.root.model.geography.PhoneNumberType;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER)
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	private static final long serialVersionUID = -462780912429013933L;

	@Getter private final String reportPointOfSale = "pos";
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private FileBusiness fileBusiness;
	@Inject private DivisionTypeBusiness divisionTypeBusiness;
	@Inject private DivisionBusiness divisionBusiness;
	
	@Inject private GenericDao genericDao;
	
	
	@Override
	protected void initialisation() {
		super.initialisation();
		registerResourceBundle("org.cyk.system.company.model.resources.entity", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.model.resources.message", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.company.business.impl.resources.message", getClass().getClassLoader());
		
		registerReportConfiguration(new ReportConfiguration<Sale, Report<SaleReport>>(reportPointOfSale) {
			@Override
			public Report<SaleReport> build(Class<Sale> aClass,Collection<Sale> sales, String fileExtension, Boolean print) {
				return saleBusiness.findReport(sales);
			}
		});
	}
	
	@Override
	public void createInitialData() {
		//structure();
		//payment();
		fakeData();
	}
	
	private void structure(){
		DivisionType department = new DivisionType(null, "DEPARTMENT", "Department");
        create(department);
        
    }
	
	private void payment(){
		File pointOfSaleReportFile = new File();
    	try {
    		pointOfSaleReportFile.setBytes(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/report/payment/pos1.jrxml")));
    		pointOfSaleReportFile.setExtension("pdf");
    		fileBusiness.create(pointOfSaleReportFile);
    		genericDao.create(pointOfSaleReportFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {
        beansMap.put((Class)Employee.class, (TypedBusiness)employeeBusiness);
        beansMap.put((Class)Customer.class, (TypedBusiness)customerBusiness);
        beansMap.put((Class)Product.class, (TypedBusiness)productBusiness);
        beansMap.put((Class)DivisionType.class, (TypedBusiness)divisionTypeBusiness);
        beansMap.put((Class)Division.class, (TypedBusiness)divisionBusiness);
        beansMap.put((Class)ProductCollection.class, (TypedBusiness)productCollectionBusiness);
        beansMap.put((Class)Sale.class, (TypedBusiness)saleBusiness);
        beansMap.put((Class)SaleCashRegisterMovement.class, (TypedBusiness)saleCashRegisterMovementBusiness);
    }
	
	/**/
	
	private void fakeData(){
		File pointOfSaleReportFile = new File();
    	try {
    		pointOfSaleReportFile.setBytes(IOUtils.toByteArray(getClass().getResourceAsStream("/org/cyk/system/company/business/impl/report/payment/pos1.jrxml")));
    		pointOfSaleReportFile.setExtension("pdf");
    		fileBusiness.create(pointOfSaleReportFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	LocalityType country=new LocalityType(null, "COUN", "Country");
        create(country);
        Locality civ = new Locality(null, country, "CIV");
        create(civ);
        PhoneNumberType pt = new PhoneNumberType("FIXE2", "Fixe");
        create(pt);
        
		Company company = new Company();
		company.setCode("C01");
		company.setName("MyCompany");
		company.getContactCollection().setPhoneNumbers(new ArrayList<PhoneNumber>());
		PhoneNumber pn = new PhoneNumber();
		pn.setCollection(company.getContactCollection());
		pn.setNumber("21232425");
		pn.setCountry(civ);
		pn.setType(pt);
		company.getContactCollection().getPhoneNumbers().add(pn);
		company.setPointOfSaleReportFile(pointOfSaleReportFile);
		company.setValueAddedTaxRate(new BigDecimal("0.0318"));
		
		companyBusiness.create(company);
		
		DivisionType department = new DivisionType(null, "DEPARTMENT", "Department");
        create(department);
        Division dept1 = create(new Division(null, department, "DEPT1"));
        Division dept2 = create(new Division(null, department, "DEPT2"));
        Division dept3 = create(new Division(null, department, "DEPT3"));
        
        IntangibleProduct itp1 = create(new IntangibleProduct("prest01","Soin de visage",dept1,null,new BigDecimal("1000")));
        IntangibleProduct itp2 = create(new IntangibleProduct("prest02","Tissage",dept2,null,new BigDecimal("3000")));
        IntangibleProduct itp3 = create(new IntangibleProduct("prest03","Pedicure",dept3,null,new BigDecimal("1500")));
        
        CashRegister cr = create(new CashRegister("CR01",company,BigDecimal.ZERO, null, null));
        
        Employee employee = new Employee();
        employee.setPerson(new Person());
        employee.getPerson().setName("Zadi");
        employeeBusiness.create(employee);
        
        create(new Cashier(employee,cr));
        
	}
	

}
