package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashierDetails extends AbstractOutputDetails<Cashier> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String person,cashRegister;
	
	public CashierDetails(Cashier cashier) {
		super(cashier);
		person = formatUsingBusiness(cashier.getPerson());
		cashRegister = formatUsingBusiness(cashier.getCashRegister());
	}
	
	/**/
	
	public static final String FIELD_PERSON = "person";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
}