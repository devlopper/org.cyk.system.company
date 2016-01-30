package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class CashRegisterMovementDetails extends AbstractOutputDetails<CashRegisterMovement> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String computedIdentifier,cashRegister;
	@IncludeInputs private MovementDetails movementDetails;
	
	public CashRegisterMovementDetails(CashRegisterMovement cashRegisterMovement) {
		super(cashRegisterMovement);
		computedIdentifier = cashRegisterMovement.getComputedIdentifier();
		cashRegister = formatUsingBusiness(cashRegisterMovement.getCashRegister());
		movementDetails = new MovementDetails(cashRegisterMovement.getMovement());
	}
}