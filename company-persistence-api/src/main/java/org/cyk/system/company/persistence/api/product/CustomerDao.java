package org.cyk.system.company.persistence.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.persistence.api.party.person.AbstractActorDao;

public interface CustomerDao extends AbstractActorDao<Customer> {

	Collection<Customer> readByBalance(BigDecimal balance);

	Collection<Customer> readByBalanceGreaterThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceLowerThanOrEquals(BigDecimal balance);

	Collection<Customer> readByBalanceNotEquals(BigDecimal balance);

   
}
