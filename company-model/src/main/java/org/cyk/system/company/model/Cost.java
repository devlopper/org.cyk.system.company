package org.cyk.system.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.Setter;

@Embeddable @Getter @Setter
public class Cost extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 4948598720219343584L;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal value = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal tax = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal turnover = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return value.toString();
	}
	
	@Override
	public String getLogMessage() {
		return String.format(DEBUG_FORMAT,value,tax,turnover);
	}
	
	private static final String DEBUG_FORMAT = "COST=%s TURNOVER=%s VAT=%s";

}
