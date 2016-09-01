package org.cyk.system.company.model.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.party.person.AbstractActorReport;

@Getter @Setter
public class EmployeeReport extends AbstractActorReport<EmployeeReport> implements Serializable {

	private static final long serialVersionUID = 7967195187341455422L;
	
}
