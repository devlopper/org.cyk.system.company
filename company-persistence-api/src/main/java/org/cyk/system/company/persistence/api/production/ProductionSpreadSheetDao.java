package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetDao extends TypedDao<ProductionSpreadSheet> {

	Collection<ProductionSpreadSheet> readByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);
	
}
