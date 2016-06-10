package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.business.impl.AbstractEnumerationDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class CashRegisterDetails extends AbstractEnumerationDetails<CashRegister> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String value;
	//@IncludeInputs private MovementCollectionDetails movementCollectionDetails;
	
	public CashRegisterDetails(CashRegister cashRegister) {
		super(cashRegister);
		value = formatNumber(cashRegister.getMovementCollection().getValue());
		//movementCollectionDetails = new MovementCollectionDetails(cashRegister.getMovementCollection());
	}
}