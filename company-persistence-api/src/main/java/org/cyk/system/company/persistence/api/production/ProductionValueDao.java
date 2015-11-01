package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionValueDao extends TypedDao<ProductionValue> {

	Collection<ProductionValue> readByProduction(Production production);

}
