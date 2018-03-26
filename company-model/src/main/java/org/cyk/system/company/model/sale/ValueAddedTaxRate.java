package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.ENUMERATION) @Accessors(chain=true)
public class ValueAddedTaxRate extends AbstractEnumeration implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;
	
	@Column(precision=PERCENT_PRECISION,scale=PERCENT_SCALE) @NotNull private BigDecimal value;
	private Boolean included = Boolean.TRUE;
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_INCLUDED = "included";
}
