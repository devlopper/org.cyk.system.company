package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.mathematics.MovementCollectionDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;

public class CashRegisterDetails extends AbstractOutputDetails<CashRegister> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@IncludeInputs private MovementCollectionDetails movementCollectionDetails;
	
	public CashRegisterDetails(CashRegister cashRegister) {
		super(cashRegister);
		movementCollectionDetails = new MovementCollectionDetails(cashRegister.getMovementCollection());
	}
}