package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductionBusiness extends TypedBusiness<Production> {

	Collection<Production> findByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	
	
}
