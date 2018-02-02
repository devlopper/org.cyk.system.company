package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.persistence.api.TypedDao;

public interface CustomerDao extends TypedDao<Customer> {
	/*
	Collection<Customer> readByBalance(BigDecimal balance);

	Collection<Customer> readByBalanceGreaterThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceLowerThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceNotEquals(BigDecimal balance);
	*/
   
}
