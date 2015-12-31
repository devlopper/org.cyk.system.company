package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.utility.common.Constant;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CashRegisterDao cashRegisterDao;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}

	@Override
	public void deposit(CashRegisterMovement movement) {
		/*exceptionUtils().exception(movement.getAmount().signum()<0, "exception.cashregister.movement.amount.deposit.invalid");
		BigDecimal balance = movement.getCashRegister().getBalance().add(movement.getAmount());
		exceptionUtils().exception(movement.getCashRegister().getMaximumBalance()!=null && balance.compareTo(movement.getCashRegister().getMaximumBalance())==1, 
				"validtion.cashregister.deposit.maximum");
		movement.getCashRegister().setBalance(balance);
		*/
		doCreate(movement,movement.getAmount(),Boolean.TRUE,movement.getCashRegister().getMaximumBalance());
	}

	@Override
	public void withdraw(CashRegisterMovement movement) {
		/*exceptionUtils().exception(movement.getAmount().signum()>0, "exception.cashregister.movement.amount.withdraw.invalid");
		BigDecimal balance = movement.getCashRegister().getBalance().subtract(movement.getAmount());
		exceptionUtils().exception(movement.getCashRegister().getMinimumBalance()!=null && balance.compareTo(movement.getCashRegister().getMinimumBalance())==-1, 
				"validtion.cashregister.deposit.minimum");
		movement.getCashRegister().setBalance(balance);
		*/
		doCreate(movement,movement.getAmount(),Boolean.FALSE,movement.getCashRegister().getMinimumBalance());
	}
	
	private void doCreate(CashRegisterMovement movement,BigDecimal amount,Boolean positive,BigDecimal limit){
		String operation = Boolean.TRUE.equals(positive) ? "deposit":"withdraw";
		BigDecimal sign = new BigDecimal((Boolean.TRUE.equals(positive) ? Constant.EMPTY_STRING:"-")+"1");
		exceptionUtils().exception(movement.getAmount().signum()<0, "exception.cashregister.movement.amount."+operation+".invalid");
		BigDecimal balance = movement.getCashRegister().getBalance().add(movement.getAmount().multiply(sign));
		exceptionUtils().exception(limit!=null && balance.compareTo(limit)==sign.intValue(), "exception.cashregister.movement.amount."+operation+".offlimit");
		
		movement.getCashRegister().setBalance(balance);
		if(movement.getDate()==null)
			movement.setDate(universalTimeCoordinated());
		logTrace(movement.getLogMessage());
		cashRegisterDao.update(movement.getCashRegister());
		dao.create(movement);
		movement.setComputedIdentifier(generateIdentifier(movement,CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFIER,accountingPeriodBusiness.findCurrent()
				.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()));
		dao.update(movement);
	}
}
