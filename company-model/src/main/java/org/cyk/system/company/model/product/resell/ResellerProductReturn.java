package org.cyk.system.company.model.product.resell;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Embeddable
public class ResellerProductReturn extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@Column(name=COLUMN_PREFIX+"quantity",precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	/**/
	
	public static final String COLUMN_PREFIX = "return_";
	
}
