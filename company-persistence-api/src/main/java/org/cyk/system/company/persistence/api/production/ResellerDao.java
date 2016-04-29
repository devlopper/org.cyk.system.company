package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.root.persistence.api.party.person.AbstractActorDao;

public interface ResellerDao extends AbstractActorDao<Reseller,Reseller.SearchCriteria> {

	Collection<Reseller> readByProductionUnit(ProductionUnit productionUnit);

}
