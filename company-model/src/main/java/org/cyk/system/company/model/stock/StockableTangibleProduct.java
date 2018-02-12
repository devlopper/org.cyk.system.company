package org.cyk.system.company.model.stock;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Deprecated
public class StockableTangibleProduct extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private TangibleProduct tangibleProduct;
	@OneToOne  @NotNull private MovementCollection movementCollection;
	
	
	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT = "tangibleProduct";
	public static final String FIELD_QUANTITY_INTERVAL = "quantityInterval";
	public static final String FIELD_MOVEMENT_COLLECTION = "movementCollection";
	
}
