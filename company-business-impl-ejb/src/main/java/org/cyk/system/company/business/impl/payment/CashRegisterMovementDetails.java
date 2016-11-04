package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.mathematics.AbstractMovementDetails;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CashRegisterMovementDetails extends AbstractMovementDetails<CashRegisterMovement,CashRegister> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String /*cashRegister,*/mode;
	//@IncludeInputs(layout=Layout.VERTICAL) private MovementDetails movement;
	
	public CashRegisterMovementDetails(CashRegisterMovement cashRegisterMovement) {
		super(cashRegisterMovement);
		//cashRegister = formatUsingBusiness(cashRegisterMovement.getCashRegister());
		//movement = new MovementDetails(cashRegisterMovement.getMovement());
		mode = formatUsingBusiness(cashRegisterMovement.getMode());
	}
	
	@Override
	protected Movement getMovement() {
		return getMaster().getMovement();
	}
	
	@Override
	protected CashRegister getCollection() {
		return master.getCashRegister();
	}
	
	public static final String FIELD_MODE = "mode";

}