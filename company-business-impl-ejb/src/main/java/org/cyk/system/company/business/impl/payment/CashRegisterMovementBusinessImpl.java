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
import org.cyk.utility.common.computation.ArithmeticOperator;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	private static final String COMPUTE_NEW_VALUE_EXCEPTION_MESSAGE_FORMAT = "exception.%s.%s.%s";
	
	@Inject private CashRegisterDao cashRegisterDao;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}

	@Override
	public void deposit(CashRegisterMovement movement) {
		doCreate(movement,movement.getCashRegister().getMaximumBalance());
	}

	@Override
	public void withdraw(CashRegisterMovement movement) {
		doCreate(movement,movement.getCashRegister().getMinimumBalance());
	}
	
	private BigDecimal increment(BigDecimal current,BigDecimal increment,BigDecimal limit,String valueNameId){
		Boolean positive = increment.signum() == 0 ? null : increment.signum() == 1 ;
		BigDecimal sign = new BigDecimal((Boolean.TRUE.equals(positive) ? Constant.EMPTY_STRING:"-")+"1");
		exceptionUtils().comparison(positive==null || increment.multiply(sign).signum() <= 0, valueNameId, ArithmeticOperator.GT, BigDecimal.ZERO);
		current = current.add(increment);
		exceptionUtils().exception(limit!=null && current.compareTo(limit)==sign.intValue(), "depasemment");
		return current;
	}
	
	private void doCreate(CashRegisterMovement movement,BigDecimal limit){
		movement.getCashRegister().setBalance(increment(movement.getCashRegister().getBalance(), movement.getAmount(), limit,"cashregister.movement.amount"));
		
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
