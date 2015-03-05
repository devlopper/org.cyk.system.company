package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.structure.CompanyDao;
import org.cyk.system.root.business.impl.party.AbstractPartyBusinessImpl;
import org.cyk.system.root.model.party.PartySearchCriteria;

public class CompanyBusinessImpl extends AbstractPartyBusinessImpl<Company, CompanyDao,PartySearchCriteria> implements CompanyBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CompanyBusinessImpl(CompanyDao dao) {
		super(dao);
	}

}
