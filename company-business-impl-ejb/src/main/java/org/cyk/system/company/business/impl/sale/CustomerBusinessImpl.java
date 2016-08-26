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
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

@Stateless
public class CustomerBusinessImpl extends AbstractActorBusinessImpl<Customer, CustomerDao,Customer.SearchCriteria> implements CustomerBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleDao saleDao;
	
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
	public Sale consume(Sale sale,Crud crud,Boolean first) {
		if(sale.getCustomer()==null){
			
		}else{
			BigDecimal sign = null;
			if(Crud.CREATE.equals(crud)){
				sign = BigDecimal.ONE;
			}else if(Crud.UPDATE.equals(crud)) {
				sign = BigDecimal.ONE;
			}else if(Crud.DELETE.equals(crud)) {
				sign = BigDecimal.ONE.negate();
			}
			commonUtils.increment(BigDecimal.class, sale.getCustomer(), Customer.FIELD_SALE_COUNT, BigDecimal.ONE.multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getCustomer(), Customer.FIELD_TURNOVER, sale.getCost().getTurnover().multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getCustomer(), Customer.FIELD_BALANCE, sale.getBalance().getValue().multiply(sign));
			
			dao.update(sale.getCustomer());	
		
			if(Crud.CREATE.equals(crud) || Crud.UPDATE.equals(crud)){
				sale.getBalance().setCumul(sale.getCustomer().getBalance());//to keep track of evolution
				sale = saleDao.update(sale);
			}
			
		}
		
		return sale;
	}
	
	/**/
	
	public static class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
		private static final long serialVersionUID = 5585791722273454192L;
		
		@Override
		public void processOnConsume(Sale sale, Crud crud, Boolean first) {
			inject(CustomerBusiness.class).consume(sale,crud,first);
		}
	}

}
