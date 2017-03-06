package org.cyk.system.company.persistence.api.structure;

import java.util.Collection;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.party.AbstractPartyDao;

public interface CompanyDao extends AbstractPartyDao<Company> {

	Collection<Company> readByManager(Person person);
	
   
}
