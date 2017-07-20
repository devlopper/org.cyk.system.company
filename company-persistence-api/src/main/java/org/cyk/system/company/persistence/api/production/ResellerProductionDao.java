package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ResellerProductionDao extends TypedDao<ResellerProduction> {

	Collection<ResellerProduction> readByProduction(Production production);

	

   
}
