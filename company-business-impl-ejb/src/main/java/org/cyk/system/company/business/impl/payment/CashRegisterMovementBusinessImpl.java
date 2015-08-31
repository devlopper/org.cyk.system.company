package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CashRegisterDao cashRegisterDao;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}

	@Override
	public void deposit(CashRegisterMovement movement) {
		exceptionUtils().exception(movement.getAmount().signum()<0, "validation.cashregister.deposit.invalid");
		movement.getCashRegister().setBalance(movement.getCashRegister().getBalance().add(movement.getAmount()));
		exceptionUtils().exception(
				movement.getCashRegister().getMaximumBalance()!=null && movement.getCashRegister().getBalance().compareTo(movement.getCashRegister().getMaximumBalance())==1, 
				"validtion.cashregister.deposit.maximum");
		doCreate(movement);
	}

	@Override
	public void withdraw(CashRegisterMovement movement) {
		exceptionUtils().exception(movement.getAmount().signum()>0, "validtion.cashregister.withdraw.invalid");
		movement.getCashRegister().setBalance(movement.getCashRegister().getBalance().subtract(movement.getAmount()));
		exceptionUtils().exception(
				movement.getCashRegister().getMinimumBalance()!=null && movement.getCashRegister().getBalance().compareTo(movement.getCashRegister().getMinimumBalance())==-1, 
				"validtion.cashregister.deposit.minimum");
		doCreate(movement);
	}
	
	private void doCreate(CashRegisterMovement movement){
		if(movement.getDate()==null)
			movement.setDate(universalTimeCoordinated());
		cashRegisterDao.update(movement.getCashRegister());
		dao.create(movement);
		movement.setComputedIdentifier(generateStringValue(CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFIER, movement));
		dao.update(movement);
	}
}
