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
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleDao saleDao;
	@Inject private CashRegisterMovementBusiness cashRegisterMovementBusiness;

	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}

	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement) {
		Integer soldOut = BigDecimal.ZERO.compareTo(saleCashRegisterMovement.getSale().getBalance());
		Boolean deposit = saleCashRegisterMovement.getCashRegisterMovement().getAmount().signum()==1;
		if(Boolean.TRUE.equals(deposit)){
			exceptionUtils().exception(soldOut>=0, "validtion.sale.soldout.yes");
			cashRegisterMovementBusiness.deposit(saleCashRegisterMovement.getCashRegisterMovement());
		}else{
			exceptionUtils().exception(soldOut<0, "validtion.sale.soldout.no");
			cashRegisterMovementBusiness.withdraw(saleCashRegisterMovement.getCashRegisterMovement());
		}
		saleCashRegisterMovement.getSale().setBalance(saleCashRegisterMovement.getSale().getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
		saleDao.update(saleCashRegisterMovement.getSale());
		return super.create(saleCashRegisterMovement);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement movement) {
		BigDecimal balance = movement.getSale().getBalance()==null?movement.getSale().getCost():movement.getSale().getBalance();
		if(movement.getCashRegisterMovement().getAmount().signum()==-1)//withdraw
			if(balance.signum()==1)
				return balance.subtract(movement.getCashRegisterMovement().getAmount());
			else
				return balance.add(movement.getCashRegisterMovement().getAmount().abs());
		//System.out.println("SaleCashRegisterMovementBusinessImpl.computeBalance() : "+balance);
		return balance.subtract(movement.getCashRegisterMovement().getAmount());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleCashRegisterMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
	
	@Override
	public void in(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	
	@Override
	public void out(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
}
