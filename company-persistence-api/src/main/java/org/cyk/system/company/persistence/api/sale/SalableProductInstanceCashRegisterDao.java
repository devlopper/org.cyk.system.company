package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SalableProductInstanceCashRegisterDao extends TypedDao<SalableProductInstanceCashRegister> {
	
	Collection<SalableProductInstanceCashRegister> readByCriteria(SearchCriteria searchCriteria);
	Long countByCriteria(SearchCriteria searchCriteria);

}
