package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
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
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class StockableTangibleProduct extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_TANGIBLE_PRODUCT,unique=true) @NotNull private TangibleProduct tangibleProduct;
	
	@Transient private MovementCollection quantityMovementCollection;
	@Transient private BigDecimal quantityMovementCollectionInitialValue;
	
	/**/
	
	public StockableTangibleProduct setQuantityMovementCollectionValue(BigDecimal value){
		if(quantityMovementCollection == null){
			
		}else
			quantityMovementCollection.setValue(value);
		return this;
	}
	
	public StockableTangibleProduct setTangibleProductFromCode(String code){
		tangibleProduct = getFromCode(TangibleProduct.class, code);
		return this;
	}
	
	public StockableTangibleProduct setQuantityMovementCollectionInitialValueFromObject(Object object){
		quantityMovementCollectionInitialValue = getNumberFromObject(BigDecimal.class, object);
		return this;
	}
	
	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT = "tangibleProduct";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION = "quantityMovementCollection";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "quantityMovementCollectionInitialValue";
	
	public static final String COLUMN_TANGIBLE_PRODUCT = FIELD_TANGIBLE_PRODUCT;
	
	/**/
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<StockableTangibleProduct> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
}
