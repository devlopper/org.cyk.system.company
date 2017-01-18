package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.impl.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifierDetails;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifier;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductInstanceCashRegisterStateLogDetails extends FiniteStateMachineStateIdentifiableGlobalIdentifierDetails implements Serializable {

	private static final long serialVersionUID = 4745377947579721936L;
	
	private SalableProductInstanceCashRegister salableProductInstanceCashRegister;
	
	@Input @InputText
	private String code,quantity,unitPrice,totalPrice,cashRegister;
	
	public SalableProductInstanceCashRegisterStateLogDetails(FiniteStateMachineStateIdentifiableGlobalIdentifier finiteStateMachineStateIdentifiableGlobalIdentifier) {
		super(finiteStateMachineStateIdentifiableGlobalIdentifier);
		
	}

	public static final String FIELD_INSTANCE_CODE = "code";
	public static final String FIELD_INSTANCE_QUANTITY = "quantity";
	public static final String FIELD_INSTANCE_UNIT_PRICE = "unitPrice";
	public static final String FIELD_INSTANCE_TOTAL_PRICE = "totalPrice";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
	
}
