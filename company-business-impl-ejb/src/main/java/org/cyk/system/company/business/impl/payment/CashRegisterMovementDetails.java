package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashRegisterMovementDetails extends AbstractOutputDetails<CashRegisterMovement> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String cashRegister,mode;
	@IncludeInputs(layout=Layout.VERTICAL) private MovementDetails movement;
	
	public CashRegisterMovementDetails(CashRegisterMovement cashRegisterMovement) {
		super(cashRegisterMovement);
		cashRegister = formatUsingBusiness(cashRegisterMovement.getCashRegister());
		movement = new MovementDetails(cashRegisterMovement.getMovement());
		mode = formatUsingBusiness(cashRegisterMovement.getMode());
	}
	
	public static final String FIELD_CASH_REGISTER = "cashRegister";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_MODE = "mode";
}