package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentDetails extends AbstractOutputDetails<SaleCashRegisterMovement> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText
	private String identifier,paid,date;
	
	public PaymentDetails(SaleCashRegisterMovement payment) {
		super(payment);
		this.identifier = payment.getCashRegisterMovement().getComputedIdentifier();
		//this.paid = numberBusiness.format(payment.getCashRegisterMovement().getAmount());
		//this.date = timeBusiness.formatDateTime(payment.getCashRegisterMovement().getDate());
	}
}