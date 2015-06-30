package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionInputDao extends TypedDao<ProductionInput> {

	Collection<ProductionInput> readByProduction(Production production);

}
