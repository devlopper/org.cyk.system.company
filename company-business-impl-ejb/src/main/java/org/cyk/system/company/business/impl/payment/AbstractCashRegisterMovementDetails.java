package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.mathematics.AbstractMovementDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public abstract class AbstractCashRegisterMovementDetails<ITEM extends AbstractIdentifiable,COLLECTION extends AbstractIdentifiable> extends AbstractMovementDetails<ITEM,COLLECTION> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String cashRegister,mode;
	
	public AbstractCashRegisterMovementDetails(ITEM item) {
		super(item);
		setMaster(item);
	}
	
	protected abstract CashRegisterMovement getCashRegisterMovement();
	
	public void setMaster(ITEM item) {
		super.setMaster(item);
		if(item==null){
			
		}else{
			if(getCashRegisterMovement().getCashRegister()==null)
				;
			else
				cashRegister = formatUsingBusiness(getCashRegister());
			
			if(getCashRegisterMovement().getMode()==null)
				;
			else
				mode = formatUsingBusiness(getCashRegisterMovement().getMode());
		}
	}
	
	@Override
	protected Movement getMovement() {
		return getCashRegisterMovement().getMovement();
	}
	
	public static final String FIELD_CASH_REGISTER = "cashRegister";
	public static final String FIELD_MODE = "mode";

}