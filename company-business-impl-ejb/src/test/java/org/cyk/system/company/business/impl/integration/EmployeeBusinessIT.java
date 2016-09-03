package org.cyk.system.company.business.impl.integration;

import java.util.Collection;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.file.FileIdentifiableGlobalIdentifierDao;

public class EmployeeBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
      
    @Override
    protected void businesses() {
    	Employee employee = inject(EmployeeBusiness.class).instanciateOne();
    	employee.setCode("A");
    	create(employee);
    	
    	CreateReportFileArguments<Employee> arguments = new CreateReportFileArguments<Employee>(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT, employee);
    	inject(EmployeeBusiness.class).createReportFile(employee, arguments);
    	FileIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
    	searchCriteria.addRepresentationType(arguments.getFile().getRepresentationType());
    	Collection<FileIdentifiableGlobalIdentifier> fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
    	assertEquals(1, fileIdentifiableGlobalIdentifiers.size());
    	
    	arguments = new CreateReportFileArguments<Employee>(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT, employee);
    	inject(EmployeeBusiness.class).createReportFile(employee, arguments);
    	searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
    	searchCriteria.addRepresentationType(arguments.getFile().getRepresentationType());
    	fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
    	assertEquals(1, fileIdentifiableGlobalIdentifiers.size());
    	
    	inject(RootBusinessTestHelper.class).write(fileIdentifiableGlobalIdentifiers.iterator().next().getFile());
    }
    
}
