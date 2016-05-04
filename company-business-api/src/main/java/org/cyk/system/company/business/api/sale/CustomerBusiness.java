package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness;

public interface CustomerBusiness extends AbstractActorBusiness<Customer,Customer.SearchCriteria> {

	Collection<Customer> findByBalance(BigDecimal balance);
	Collection<Customer> findByBalanceNotEquals(BigDecimal balance);
	Collection<Customer> findByBalanceGreaterThanOrEquals(BigDecimal balance);
	Collection<Customer> findByBalanceLowerThanOrEquals(BigDecimal balance);
	
	Sale consume(Sale sale);
	
}
