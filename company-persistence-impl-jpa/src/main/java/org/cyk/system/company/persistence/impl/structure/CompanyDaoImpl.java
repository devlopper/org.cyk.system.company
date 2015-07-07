package org.cyk.system.company.persistence.impl.structure;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.structure.CompanyDao;
import org.cyk.system.root.model.party.PartySearchCriteria;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.impl.party.AbstractPartyDaoImpl;

public class CompanyDaoImpl extends AbstractPartyDaoImpl<Company,PartySearchCriteria> implements CompanyDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByManager;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByManager, _select().where("manager"));
	}
	
	@Override
	public Collection<Company> readByManager(Person manager) {
		return namedQuery(readByManager).parameter("manager", manager).resultMany();
	}

}
