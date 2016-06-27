package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.impl.mathematics.machine.FiniteStateMachineStateLogDetails;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateLog;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductInstanceCashRegisterStateLogDetails extends FiniteStateMachineStateLogDetails implements Serializable {

	private static final long serialVersionUID = 4745377947579721936L;
	
	private SalableProductInstanceCashRegister salableProductInstanceCashRegister;
	
	@Input @InputText
	private String instanceCode,instanceQuantity,instanceUnitPrice,instanceTotalPrice,cashRegister;
	
	public SalableProductInstanceCashRegisterStateLogDetails(FiniteStateMachineStateLog finiteStateMachineStateLog) {
		super(finiteStateMachineStateLog);
		
	}

}
