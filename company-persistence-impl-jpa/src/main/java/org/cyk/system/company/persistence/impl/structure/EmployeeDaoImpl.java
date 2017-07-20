package org.cyk.system.company.persistence.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.persistence.api.structure.EmployeeDao;
import org.cyk.system.root.persistence.impl.party.person.AbstractActorDaoImpl;

public class EmployeeDaoImpl extends AbstractActorDaoImpl<Employee,Employee.SearchCriteria> implements EmployeeDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

}
