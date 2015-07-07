package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ProductionSpreadSheetBusiness extends TypedBusiness<ProductionSpreadSheet> {

	Collection<ProductionSpreadSheet> findByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	
	
}
