package org.cyk.system.company.persistence.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.persistence.api.party.person.AbstractActorDao;

public interface CustomerDao extends AbstractActorDao<Customer,Customer.SearchCriteria> {

	Collection<Customer> readByBalance(BigDecimal balance);

	Collection<Customer> readByBalanceGreaterThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceLowerThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceNotEquals(BigDecimal balance);

   
}
