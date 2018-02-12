package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class TangibleProduct extends Product implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@Column(precision=10,scale=FLOAT_SCALE) @Accessors(chain=true) private BigDecimal quantity;
		
	public static final String FIELD_QUANTITY = "quantity";
}
