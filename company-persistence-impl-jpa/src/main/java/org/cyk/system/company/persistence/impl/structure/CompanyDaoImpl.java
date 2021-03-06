package org.cyk.system.company.persistence.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.structure.CompanyDao;
import org.cyk.system.root.persistence.impl.party.AbstractPartyDaoImpl;

public class CompanyDaoImpl extends AbstractPartyDaoImpl<Company> implements CompanyDao,Serializable {
	private static final long serialVersionUID = -1712788156426144935L;

}
