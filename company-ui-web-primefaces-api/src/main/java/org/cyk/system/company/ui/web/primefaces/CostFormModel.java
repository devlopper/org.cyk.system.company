package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Cost;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

@Getter @Setter
public class CostFormModel implements Serializable {

	private static final long serialVersionUID = -465747050467060317L;
 
	@Input @InputNumber private BigDecimal value;
	
	@Input @InputNumber private BigDecimal turnover = BigDecimal.ZERO;
	
	@Input @InputNumber private BigDecimal tax = BigDecimal.ZERO;
	
	public void set(Cost cost){
		if(cost==null)
			return;
		value = cost.getValue();
		turnover = cost.getTurnover();
		tax = cost.getTax();
	}
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_TAX = "tax";
	
}
