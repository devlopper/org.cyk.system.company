package org.cyk.system.company.business.api;

import java.util.Collection;

import org.cyk.system.company.model.Cost;

public interface CostBusiness {
	
	void add(Cost cost,Collection<Cost> costs);
	void add(Cost cost,Cost...costs);
	
}
