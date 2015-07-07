package org.cyk.system.company.business.api.structure;

import java.util.Collection;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.party.AbstractPartyBusiness;
import org.cyk.system.root.model.party.PartySearchCriteria;
import org.cyk.system.root.model.party.person.Person;

public interface CompanyBusiness extends AbstractPartyBusiness<Company,PartySearchCriteria> {

	Collection<Company> findByManager(Person manager);
	
}
