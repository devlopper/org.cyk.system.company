package org.cyk.system.company.persistence.api.structure;

import java.util.Collection;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.party.PartySearchCriteria;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.party.AbstractPartyDao;

public interface CompanyDao extends AbstractPartyDao<Company,PartySearchCriteria> {

	Collection<Company> readByManager(Person person);
	
   
}
