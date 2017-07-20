package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.annotation.user.interfaces.Text.ValueType;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter
public class CostFormModel extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -465747050467060317L;
 
	@Input(label=@Text(type=ValueType.ID,value="field.cost")) @InputNumber private BigDecimal value;
	
	@Input @InputNumber private BigDecimal turnover = BigDecimal.ZERO;
	
	@Input @InputNumber private BigDecimal tax = BigDecimal.ZERO;
	
	public void set(Cost cost){
		if(cost==null)
			return;
		value = cost.getValue();
		turnover = cost.getTurnover();
		tax = cost.getTax();
	}
	
	public void write(Cost cost,AccountingPeriod accountingPeriod,Boolean computeTax) {
		cost.setValue(value);
		if(Boolean.TRUE.equals(computeTax)){
			cost.setTax(inject(AccountingPeriodBusiness.class).computeValueAddedTax(accountingPeriod, cost.getValue()));
			cost.setTurnover(inject(AccountingPeriodBusiness.class).computeTurnover(accountingPeriod, cost.getValue(),cost.getTax()));
		}else{
			cost.setTurnover(turnover);
			cost.setTax(tax);
		}
	}
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_TAX = "tax";

	
}
