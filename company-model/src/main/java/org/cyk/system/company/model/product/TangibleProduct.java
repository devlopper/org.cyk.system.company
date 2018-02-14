package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.cyk.system.root.model.mathematics.MovementCollection;
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
	
	@Transient @Accessors(chain=true) protected MovementCollection quantityMovementCollection;
	
	public TangibleProduct setQuantity(BigDecimal value){
		quantityMovementCollection.setValue(value);
		return this;
	}
	
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION = "quantityMovementCollection";
}
