package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.persistence.api.file.FileRepresentationTypeDao;

public class EmployeeBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
      
    @Override
    protected void businesses() {
    	Employee employee = inject(EmployeeBusiness.class).instanciateOne();
    	employee.setCode("A");
    	create(employee);
    	
    	File file = new File();
    	file.setRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT));
    	inject(EmployeeBusiness.class).createFile(employee, file);
    	inject(RootBusinessTestHelper.class).write(file);
    }
    
}
