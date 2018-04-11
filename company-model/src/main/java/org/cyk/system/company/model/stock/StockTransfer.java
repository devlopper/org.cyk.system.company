package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class StockTransfer extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT_STORE,unique=true) @NotNull private ProductStore productStore;
	
	@Transient private MovementCollection quantityMovementCollection;
	@Transient private BigDecimal quantityMovementCollectionInitialValue;
	
	/**/
	
	
	
	/**/
	
	public static final String FIELD_PRODUCT_STORE = "productStore";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION = "quantityMovementCollection";
	public static final String FIELD_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "quantityMovementCollectionInitialValue";
	
	public static final String COLUMN_PRODUCT_STORE = FIELD_PRODUCT_STORE;
	
	/**/
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<StockTransfer> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
}
