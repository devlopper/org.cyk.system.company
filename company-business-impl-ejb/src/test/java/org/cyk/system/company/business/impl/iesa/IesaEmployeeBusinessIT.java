package org.cyk.system.company.business.impl.iesa;

import java.util.Collection;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementTypeDao;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.file.FileIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.file.FileRepresentationTypeDao;

public class IesaEmployeeBusinessIT extends AbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
      
    @Override
    protected void businesses() {
    	Employee employee = inject(EmployeeBusiness.class).instanciateOne();
    	employee.setCode("A");
    	employee.setEmploymentAgreement(new EmploymentAgreement());
    	employee.getEmploymentAgreement().setType(inject(EmploymentAgreementTypeDao.class).readOneRandomly());
    	create(employee);
    	
    	FileIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
    	searchCriteria.addRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT));
    	Collection<FileIdentifiableGlobalIdentifier> fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
    	assertEquals(1, fileIdentifiableGlobalIdentifiers.size());
    	
    	inject(RootBusinessTestHelper.class).write(fileIdentifiableGlobalIdentifiers.iterator().next().getFile());
    }
    
}
