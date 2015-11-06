package org.cyk.system.company.persistence.impl.production;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.persistence.api.production.ResellerProductionDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ResellerProductionDaoImpl extends AbstractTypedDao<ResellerProduction> implements ResellerProductionDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByProduction;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduction, _select().where(ResellerProduction.FIELD_PRODUCTION));
	}
	
	@Override
	public Collection<ResellerProduction> readByProduction(Production production) {
		return namedQuery(readByProduction).parameter(ResellerProduction.FIELD_PRODUCTION, production).resultMany();
	}

}
