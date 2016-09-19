package org.cyk.system.company.business.api.structure;

import java.util.Collection;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface EmploymentAgreementBusiness extends TypedBusiness<EmploymentAgreement> {

	Collection<EmploymentAgreement> findByEmployee(Employee employee);
	
}
