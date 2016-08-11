package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashRegisterMovementTermDetails extends AbstractOutputDetails<CashRegisterMovementTerm> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String amount,event;
	
	public CashRegisterMovementTermDetails(CashRegisterMovementTerm cashRegisterMovementTerm) {
		super(cashRegisterMovementTerm);
		amount = formatNumber(cashRegisterMovementTerm.getAmount());
		event = formatUsingBusiness(cashRegisterMovementTerm.getEvent());
	}
	
	/**/
	
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_EVENT = "event";
}