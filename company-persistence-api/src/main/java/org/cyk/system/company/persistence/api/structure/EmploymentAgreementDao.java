package org.cyk.system.company.persistence.api.structure;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface EmploymentAgreementDao extends TypedDao<EmploymentAgreement> {

	EmploymentAgreement readByEmployee(Employee employee);
}
