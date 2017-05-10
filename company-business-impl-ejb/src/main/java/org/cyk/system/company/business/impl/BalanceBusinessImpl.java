package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.BalanceBusiness;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.model.mathematics.MovementAction;

public class BalanceBusinessImpl implements BalanceBusiness, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Balance computeCurrent(SaleCashRegisterMovement saleCashRegisterMovement) {
		return compute(saleCashRegisterMovement, Boolean.TRUE);
	}

	@Override
	public Balance computePrevious(SaleCashRegisterMovement saleCashRegisterMovement) {
		return compute(saleCashRegisterMovement, Boolean.FALSE);
	}

	@Override
	public Balance compute(SaleCashRegisterMovement saleCashRegisterMovement,Boolean current) {
		Balance balance = new Balance();
		BigDecimal saleCashRegisterMovementAmount = saleCashRegisterMovement.getAmount();
		balance.setValue(saleCashRegisterMovement.getSale().getIdentifier()==null?saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue()
				:saleCashRegisterMovement.getSale().getBalance().getValue());
		
		MovementAction action = saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().getAction();
		if(action==null || action.equals(saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction())){
			balance.setValue(balance.getValue().subtract(saleCashRegisterMovementAmount));
		}else{
			if(balance.getValue().signum()==1)
				balance.setValue(balance.getValue().subtract(saleCashRegisterMovementAmount));
			else
				balance.setValue(balance.getValue().add(saleCashRegisterMovementAmount.abs()));
		}
		return balance;
	}
	
}
