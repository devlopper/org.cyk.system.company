package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness;

public interface ResellerBusiness extends AbstractActorBusiness<Reseller,Reseller.SearchCriteria> {

	Collection<Reseller> findByProductionUnit(ProductionUnit productionUnit);
	
}
