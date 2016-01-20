package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractModelElement;

@Getter @Setter @Embeddable
public class SaleResults extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 2700928054823690772L;

	@Embedded private Cost cost = new Cost();
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal balance = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal paid = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}

	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,cost.getLogMessage());
	}
	
	private static final String LOG_FORMAT = SaleResults.class.getSimpleName()+"(%s)";
	
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_PAID = "paid";
}
