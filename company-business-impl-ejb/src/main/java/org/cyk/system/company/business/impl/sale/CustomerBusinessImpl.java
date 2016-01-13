package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

@Stateless
public class CustomerBusinessImpl extends AbstractActorBusinessImpl<Customer, CustomerDao> implements CustomerBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CustomerBusinessImpl(CustomerDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Customer> findByBalance(BigDecimal balance) {
		return dao.readByBalance(balance);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Customer> findByBalanceNotEquals(BigDecimal balance) {
		return dao.readByBalanceNotEquals(balance);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Customer> findByBalanceGreaterThanOrEquals(BigDecimal balance) {
		return dao.readByBalanceGreaterThanOrEquals(balance);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Customer> findByBalanceLowerThanOrEquals(BigDecimal balance) {
		return dao.readByBalanceLowerThanOrEquals(balance);
	}
	
	@Override
	public void consume(Sale sale) {
		Customer customer = sale.getCustomer();
		customer.setSaleCount(customer.getSaleCount().add(BigDecimal.ONE));
		customer.setTurnover(customer.getTurnover().add(sale.getCost().getTurnover()));
		customer.setBalance(customer.getBalance().add(sale.getBalance().getValue()));
		dao.update(customer);	
	}

}
