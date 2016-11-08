package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Balance;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

@Getter @Setter
public class BalanceFormModel implements Serializable {

	private static final long serialVersionUID = -465747050467060317L;
 
	@Input @InputNumber private BigDecimal value;
	
	@Input @InputNumber private BigDecimal cumul;
	
	public void set(Balance balance){
		if(balance==null)
			return;
		value = balance.getValue();
		cumul = balance.getCumul();
	}
	
	public void write(Balance balance) {
		balance.setValue(value);
		balance.setCumul(cumul);
	}
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_CUMUL = "cumul";

	
}
