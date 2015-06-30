package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness;

public interface CustomerBusiness extends AbstractActorBusiness<Customer> {

	Collection<Customer> findByBalance(BigDecimal balance);
	Collection<Customer> findByBalanceNotEquals(BigDecimal balance);
	Collection<Customer> findByBalanceGreaterThanOrEquals(BigDecimal balance);
	Collection<Customer> findByBalanceLowerThanOrEquals(BigDecimal balance);
	
}
