package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;

@Getter @Setter
public class CashRegisterMovementDetails extends AbstractCashRegisterMovementDetails<CashRegisterMovement,CashRegister> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	public CashRegisterMovementDetails(CashRegisterMovement cashRegisterMovement) {
		super(cashRegisterMovement);
	}
	
	@Override
	protected CashRegister getCollection() {
		return master.getCashRegister();
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return master;
	}

}