package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
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
public class StockableProduct extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT,unique=true) @NotNull private Product product;
	
	@Transient private MovementCollection quantityMovementCollection;
	@Transient private BigDecimal quantityMovementCollectionInitialValue;
	
	@Transient private Class<? extends Product> productClass;
	
	/**/
	
	@Override
	public StockableProduct setCode(String code) {
		return (StockableProduct) super.setCode(code);
	}
	
	public StockableProduct setQuantityMovementCollectionValue(BigDecimal value){
		if(quantityMovementCollection == null){
			
		}else
			quantityMovementCollection.setValue(value);
		return this;
	}
	
	public StockableProduct setProductFromCode(String code){
		product = getFromCode(TangibleProduct.class, code);
		return this;
	}
	
	public StockableProduct setQuantityMovementCollectionInitialValueFromObject(Object object){
		quantityMovementCollectionInitialValue = getNumberFromObject(BigDecimal.class, object);
		return this;
	}
	
	@Override
	public StockableProduct addCascadeOperationToMasterFieldNames(String... fieldNames) {
		return (StockableProduct) super.addCascadeOperationToMasterFieldNames(fieldNames);
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION = "quantityMovementCollection";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "quantityMovementCollectionInitialValue";
	
	public static final String COLUMN_PRODUCT = FIELD_PRODUCT;
	
	/**/
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<StockableProduct> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
}
