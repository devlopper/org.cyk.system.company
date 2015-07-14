package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.persistence.api.structure.EmployeeDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

@Stateless
public class EmployeeBusinessImpl extends AbstractActorBusinessImpl<Employee, EmployeeDao> implements EmployeeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public EmployeeBusinessImpl(EmployeeDao dao) {
		super(dao);
	}

}
