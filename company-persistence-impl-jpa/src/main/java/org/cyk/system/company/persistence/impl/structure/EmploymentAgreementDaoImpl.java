package org.cyk.system.company.persistence.impl.structure;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class EmploymentAgreementDaoImpl extends AbstractTypedDao<EmploymentAgreement> implements EmploymentAgreementDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByEmployee;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByEmployee, _select().where(EmploymentAgreement.FIELD_EMPLOYEE));
	}
	
	@Override
	public Collection<EmploymentAgreement> readByEmployee(Employee employee) {
		return namedQuery(readByEmployee).parameter(EmploymentAgreement.FIELD_EMPLOYEE, employee).resultMany();
	}

	

}
