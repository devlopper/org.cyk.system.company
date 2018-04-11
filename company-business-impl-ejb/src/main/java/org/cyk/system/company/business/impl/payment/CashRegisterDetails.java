package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.business.impl.AbstractEnumerationDetails;
import org.cyk.system.root.business.impl.mathematics.movement.MovementCollectionDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashRegisterDetails extends AbstractEnumerationDetails<CashRegister> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String value;
	@IncludeInputs private MovementCollectionDetails movementCollection;
	
	public CashRegisterDetails(CashRegister cashRegister) {
		super(cashRegister);
		value = formatNumber(cashRegister.getMovementCollection().getValue());
		movementCollection = new MovementCollectionDetails(cashRegister.getMovementCollection());
	}
	
	/**/
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_MOVEMENT_COLLECTION = "movementCollection";
}