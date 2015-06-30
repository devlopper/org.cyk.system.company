package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

@Stateless
public class CustomerBusinessImpl extends AbstractActorBusinessImpl<Customer, CustomerDao> implements CustomerBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CustomerBusinessImpl(CustomerDao dao) {
		super(dao);
	}

	@Override
	public Collection<Customer> findByBalance(BigDecimal balance) {
		return dao.readByBalance(balance);
	}
	
	@Override
	public Collection<Customer> findByBalanceNotEquals(BigDecimal balance) {
		return dao.readByBalanceNotEquals(balance);
	}
	
	@Override
	public Collection<Customer> findByBalanceGreaterThanOrEquals(BigDecimal balance) {
		return dao.readByBalanceGreaterThanOrEquals(balance);
	}
	
	@Override
	public Collection<Customer> findByBalanceLowerThanOrEquals(BigDecimal balance) {
		return dao.readByBalanceLowerThanOrEquals(balance);
	}

}
