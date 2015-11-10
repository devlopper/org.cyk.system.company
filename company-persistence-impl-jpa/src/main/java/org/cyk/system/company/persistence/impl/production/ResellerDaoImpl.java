package org.cyk.system.company.persistence.impl.production;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.persistence.api.production.ResellerDao;
import org.cyk.system.root.persistence.impl.party.person.AbstractActorDaoImpl;

public class ResellerDaoImpl extends AbstractActorDaoImpl<Reseller> implements ResellerDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

	private String readByProductionUnit;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductionUnit, _select().where(Reseller.FIELD_PRODUCTION_UNIT));
	}
	
	@Override
	public Collection<Reseller> readByProductionUnit(ProductionUnit productionUnit) {
		return namedQuery(readByProductionUnit).parameter(Reseller.FIELD_PRODUCTION_UNIT, productionUnit).resultMany();
	}

}
