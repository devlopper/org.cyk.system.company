package org.cyk.system.company.business.api;

import org.cyk.system.company.model.Cost;

public interface CostBusiness {
	
	void add(Cost cost,Cost...costs);
	
}
