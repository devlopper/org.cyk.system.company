package org.cyk.system.company.business.impl.iesa;

import java.util.Collection;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementTypeDao;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.file.FileDao;
import org.cyk.system.root.persistence.api.file.FileIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.file.FileRepresentationTypeDao;

public class IesaEmployeeBusinessIT extends AbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
      
    @Override
    protected void businesses() {
    	Employee employee = inject(EmployeeBusiness.class).instanciateOne();
    	employee.setCode("EMP001");
    	employee.setName("Zeze");
    	employee.getPerson().setLastnames("Marius");
    	employee.setEmploymentAgreement(new EmploymentAgreement());
    	employee.getEmploymentAgreement().setType(inject(EmploymentAgreementTypeDao.class).readOneRandomly());
    	create(employee);
    	Long fileCount = inject(FileDao.class).countAll();
    	inject(EmployeeBusiness.class).createReportFile(new CreateReportFileArguments.Builder<Employee>(employee).setReportTemplate(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT).build());
    	assertEquals(fileCount+1, inject(FileDao.class).countAll());
    	
    	FileIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
    	searchCriteria.addRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT));
    	Collection<FileIdentifiableGlobalIdentifier> fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
    	assertEquals(1, fileIdentifiableGlobalIdentifiers.size());
    	inject(RootBusinessTestHelper.class).write(fileIdentifiableGlobalIdentifiers.iterator().next().getFile());
    	
    	employee = inject(EmployeeBusiness.class).find("EMP001");
    	employee.setName("Komenan");
    	employee.getPerson().setLastnames("Yao christian");
    	employee = inject(EmployeeBusiness.class).update(employee);
    	
    	fileCount = inject(FileDao.class).countAll();
    	inject(EmployeeBusiness.class).createReportFile(new CreateReportFileArguments.Builder<Employee>(employee).setReportTemplate(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT).build());
    	assertEquals(fileCount, inject(FileDao.class).countAll());
    	
    	searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
    	searchCriteria.addRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT));
    	fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
    	assertEquals(1, fileIdentifiableGlobalIdentifiers.size());
    	inject(RootBusinessTestHelper.class).write(fileIdentifiableGlobalIdentifiers.iterator().next().getFile());
    }
    
}
