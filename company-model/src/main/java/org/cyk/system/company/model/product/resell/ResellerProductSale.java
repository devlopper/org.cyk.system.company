package org.cyk.system.company.model.product.resell;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.system.root.model.ValueDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Embeddable
public class ResellerProductSale extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@Column(name=COLUMN_PREFIX+"quantity",precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity = BigDecimal.ZERO;
	
	@Embedded private ResellerProductReturn productReturn = new ResellerProductReturn();
	
	@AttributeOverrides(value={
			@AttributeOverride(name="user",column=@Column(name=COLUMN_PREFIX+"amount_user"))
			,@AttributeOverride(name="system",column=@Column(name=COLUMN_PREFIX+"amount_computed"))
			,@AttributeOverride(name="gap",column=@Column(name=COLUMN_PREFIX+"amount_gap"))
	})
	@Embedded private ValueDetails amount = new ValueDetails();
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	public static final String COLUMN_PREFIX = "sale_";
	
}
