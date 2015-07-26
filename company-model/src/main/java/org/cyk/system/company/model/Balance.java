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
public class Balance extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 4330380476164276099L;

	@Column(name="balance_value",precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal value = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal cumul;
	
	@Override
	public String getUiString() {
		return toString();
	}

}
