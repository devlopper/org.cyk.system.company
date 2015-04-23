package org.cyk.system.company.model.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractModelElement;

@Getter @Setter @Embeddable @NoArgsConstructor @AllArgsConstructor
public class SalesResults extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = -344733168050282039L;
	
	@Column(name="numberOfSales",precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal count = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal turnover = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal valueAddedTaxes = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return count+"/"+turnover+"/"+valueAddedTaxes;
	}
	
	@Override
	public String toString() {
		return getUiString();
	}
	
}
