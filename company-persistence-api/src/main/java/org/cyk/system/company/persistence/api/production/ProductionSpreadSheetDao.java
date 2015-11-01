package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ProductionSpreadSheetDao extends TypedDao<Production> {

	Collection<Production> readByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);

	Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria);
	
}
