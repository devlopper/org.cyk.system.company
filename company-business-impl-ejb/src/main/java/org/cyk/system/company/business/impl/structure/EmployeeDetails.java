package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.impl.party.person.AbstractActorDetails;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeDetails extends AbstractActorDetails.AbstractDefault<Employee> implements Serializable {

	private static final long serialVersionUID = -1498269103849317057L;
	
	public EmployeeDetails(Employee employee) {
		super(employee);
	}
	
	
	
}