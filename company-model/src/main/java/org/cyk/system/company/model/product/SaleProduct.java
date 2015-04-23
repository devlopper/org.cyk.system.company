package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class SaleProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne private Sale sale;
	@ManyToOne private Product product;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal reduction=BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal price;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal turnover;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal valueAddedTax;

	public SaleProduct(Sale sale,Product product,BigDecimal quantity) {
		super();
		this.sale = sale;
		this.product = product;
		this.quantity = quantity;
	}
	
	@Override
	public String getUiString() {
		return product.getUiString();
	}
	
	@Override
	public String toString() {
		return getUiString();
	}

}
