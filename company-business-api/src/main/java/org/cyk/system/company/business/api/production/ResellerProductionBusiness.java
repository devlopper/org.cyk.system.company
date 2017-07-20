package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ResellerProductionBusiness extends TypedBusiness<ResellerProduction> {

	Collection<ResellerProduction> findByProduction(Production production);
	
}
