package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.system.company.model.Balance;
import org.cyk.system.root.business.impl.AbstractModelElementOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class BalanceDetails extends AbstractModelElementOutputDetails<Balance> implements Serializable {

	@Input @InputText private String value,cumul;
	
	private static final long serialVersionUID = 8350394829223603259L;

	public BalanceDetails(Balance balance) {
		super(balance);
		set(balance);
	}
	
	public void set(Balance balance){
		if(balance==null){
			
		}else{
			value = formatNumber(balance.getValue());
			cumul = formatNumber(balance.getCumul());
		}
	}
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_CUMUL = "cumul";

}
