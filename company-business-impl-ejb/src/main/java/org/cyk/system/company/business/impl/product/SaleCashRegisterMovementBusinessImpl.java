package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleDao saleDao;
	@Inject private CustomerDao customerDao;
	@Inject private CashRegisterMovementBusiness cashRegisterMovementBusiness;

	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}

	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Create sale cash register movement");
		Sale sale = saleCashRegisterMovement.getSale();
		Customer customer = sale.getCustomer();
		Integer soldOut = BigDecimal.ZERO.compareTo(sale.getBalance());
		Boolean deposit = saleCashRegisterMovement.getCashRegisterMovement().getAmount().signum()>=0;
		if(Boolean.TRUE.equals(deposit)){
			exceptionUtils().exception(soldOut>=0, "validtion.sale.soldout.yes");
			cashRegisterMovementBusiness.deposit(saleCashRegisterMovement.getCashRegisterMovement());
		}else{
			exceptionUtils().exception(soldOut<0, "validtion.sale.soldout.no");
			cashRegisterMovementBusiness.withdraw(saleCashRegisterMovement.getCashRegisterMovement());
		}
		sale.setBalance(sale.getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
		saleDao.update(sale);
		
		if(customer!=null){
			Boolean firstSaleCashRegisterMovement = dao.countBySale(sale)==0;
			if(firstSaleCashRegisterMovement){
				customer.setBalance(customer.getBalance().add(sale.getBalance()));
			}else{
				customer.setBalance(customer.getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
			}
			
			if(Boolean.TRUE.equals(deposit)){
				customer.setPaid(customer.getPaid().add(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
			}else{
				//TODO something to be done??? I do not know
			}
			
			customerDao.update(customer);
			sale.getCustomer().setTurnover(customer.getTurnover());// Because of live object in test case
			sale.getCustomer().setPaid(customer.getPaid());
			sale.getCustomer().setBalance(customer.getBalance());
		}
		logDebug("Sale cash register movement created successfully. I={} O={} A={} CT={} CP={} CB={}",saleCashRegisterMovement.getAmountIn(),
				saleCashRegisterMovement.getAmountOut(),saleCashRegisterMovement.getCashRegisterMovement().getAmount(),customer==null?"":customer.getTurnover()
						,customer==null?"":customer.getPaid(),customer==null?"":customer.getPaid());
		return super.create(saleCashRegisterMovement);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement movement) {
		BigDecimal balance = movement.getSale().getIdentifier()==null/*movement.getSale().getBalance()==null*/?movement.getSale().getCost():movement.getSale().getBalance();
		if(movement.getCashRegisterMovement().getAmount().signum()==-1)//withdraw
			if(balance.signum()==1)
				return balance.subtract(movement.getCashRegisterMovement().getAmount());
			else
				return balance.add(movement.getCashRegisterMovement().getAmount().abs());
		return balance.subtract(movement.getCashRegisterMovement().getAmount());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleCashRegisterMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void in(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void out(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
}
