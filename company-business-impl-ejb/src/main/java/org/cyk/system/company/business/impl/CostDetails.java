package org.cyk.system.company.business.impl;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.business.impl.AbstractModelElementOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CostDetails extends AbstractModelElementOutputDetails<Cost> implements Serializable {

	private static final long serialVersionUID = 8350394829223603259L;

	@Input @InputText private String value,turnover,tax;
	
	public CostDetails(Cost cost) {
		super(cost);
		set(cost);
	}
	
	public void set(Cost cost) {
		if(cost==null){
			
		}else{
			value = formatNumber(cost.getValue());
			turnover = formatNumber(cost.getTurnover());
			tax = formatNumber(cost.getTax());	
		}
		
	}
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_TAX = "tax";

}
