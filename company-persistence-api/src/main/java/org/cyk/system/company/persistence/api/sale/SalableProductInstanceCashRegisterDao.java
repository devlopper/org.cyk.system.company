package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SalableProductInstanceCashRegisterDao extends TypedDao<SalableProductInstanceCashRegister> {

	SalableProductInstanceCashRegister readBySalableProductInstanceByCashRegister(SalableProductInstance salableProductInstance,CashRegister cashRegister);
	
	Collection<SalableProductInstanceCashRegister> readByCriteria(SearchCriteria searchCriteria);
	Long countByCriteria(SearchCriteria searchCriteria);

}
