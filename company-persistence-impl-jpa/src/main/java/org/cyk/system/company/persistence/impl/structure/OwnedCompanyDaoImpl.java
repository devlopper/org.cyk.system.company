package org.cyk.system.company.persistence.impl.structure;

import java.io.Serializable;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.structure.OwnedCompanyDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class OwnedCompanyDaoImpl extends AbstractTypedDao<OwnedCompany> implements OwnedCompanyDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readBySelected;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySelected, _select().where("selected"));
	}
	
	@Override
	public OwnedCompany readBySelected(Boolean selected) {
		return namedQuery(readBySelected).parameter("selected", selected).ignoreThrowable(NoResultException.class).resultOne();
	}

}
