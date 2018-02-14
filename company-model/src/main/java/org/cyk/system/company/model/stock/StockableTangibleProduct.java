package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class StockableTangibleProduct extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_TANGIBLE_PRODUCT) @NotNull @Accessors(chain=true) private TangibleProduct tangibleProduct;
	
	@Transient @Accessors(chain=true) protected MovementCollection quantityMovementCollection;
	
	/**/
	
	public StockableTangibleProduct setQuantityMovementCollectionValue(BigDecimal value){
		if(quantityMovementCollection == null){
			
		}else
			quantityMovementCollection.setValue(value);
		return this;
	}
	
	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT = "tangibleProduct";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION = "quantityMovementCollection";
	
	public static final String COLUMN_TANGIBLE_PRODUCT = FIELD_TANGIBLE_PRODUCT;
	
}
