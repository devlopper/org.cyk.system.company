package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleCashRegisterMovementDetails extends AbstractOutputDetails<SaleCashRegisterMovement> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@IncludeInputs private CashRegisterMovementDetails cashRegisterMovementDetails;
	
	public SaleCashRegisterMovementDetails(SaleCashRegisterMovement saleCashRegisterMovement) {
		super(saleCashRegisterMovement);
		cashRegisterMovementDetails = new CashRegisterMovementDetails(saleCashRegisterMovement.getCashRegisterMovement());
	}
}