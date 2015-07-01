package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductionInputBusiness extends TypedBusiness<ProductionInput> {

	Collection<ProductionInput> findByProduction(Production production);
	
}
