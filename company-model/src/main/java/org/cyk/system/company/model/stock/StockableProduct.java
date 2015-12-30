package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class StockableProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Product product;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal currentQuantity = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal useQuantity = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal usedQuantity = BigDecimal.ZERO;
	
	/* Alert */
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal minimalQuantityAlert = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal minimalQuantityBlock = BigDecimal.ZERO;
	
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_PRICE = "price";
}
