package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductInstanceCashRegisterDetails extends AbstractOutputDetails<SalableProductInstanceCashRegister> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String salableProductInstance,cashRegister,finiteStateMachineState;
	
	public SalableProductInstanceCashRegisterDetails(SalableProductInstanceCashRegister salableProductInstanceCashRegister) {
		super(salableProductInstanceCashRegister);
		this.salableProductInstance = salableProductInstanceCashRegister.getSalableProductInstance().getCode();// formatUsingBusiness(salableProductInstanceCashRegister.getSalableProductInstance());
		this.cashRegister = formatUsingBusiness(salableProductInstanceCashRegister.getCashRegister());
		this.finiteStateMachineState = formatUsingBusiness(salableProductInstanceCashRegister.getFiniteStateMachineState());
	}
}