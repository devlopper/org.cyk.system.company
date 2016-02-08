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

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class StockableTangibleProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private TangibleProduct tangibleProduct;
	@OneToOne  @NotNull private MovementCollection movementCollection;
	
	//@JoinColumn @OneToOne @NotNull private Interval quantityInterval;
	
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal currentQuantity = BigDecimal.ZERO;
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal useQuantity = BigDecimal.ZERO;
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal usedQuantity = BigDecimal.ZERO;
	
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal minimalQuantityAlert = BigDecimal.ZERO;
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal minimalQuantityBlock = BigDecimal.ZERO;
	
	/**/
	
	@Override
	public String getUiString() {
		return tangibleProduct.getUiString();
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT, tangibleProduct.getCode(),movementCollection.getLogMessage());
	}
	
	private static final String LOG_FORMAT = StockableTangibleProduct.class.getSimpleName()+"(TP=%S %s)";
	
	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT = "tangibleProduct";
	public static final String FIELD_QUANTITY_INTERVAL = "quantityInterval";
	public static final String FIELD_MOVEMENT_COLLECTION = "movementCollection";
	
}
