package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.structure.Division;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity 
public class TangibleProduct extends Product implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	/* Stock */
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal stockQuantity = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal useQuantity = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal usedQuantity = BigDecimal.ZERO;
	
	/* Alert */
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	@Input @InputNumber
	private BigDecimal minimalStockQuantityAlert = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	@Input @InputNumber
	private BigDecimal minimalStockQuantityBlock = BigDecimal.ZERO;
	
	public TangibleProduct(String code, String name, Division division,ProductCategory category, BigDecimal price) {
		super(code, name, division, category, price);
	}
	
	
}
