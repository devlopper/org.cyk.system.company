package org.cyk.system.company.model.product.resell;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractIdentifiable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class ResellerProductTaking extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private Reseller reseller;
	@ManyToOne @NotNull private Product product;
	@Column(name=COLUMN_PREFIX+"quantity",precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity = BigDecimal.ZERO;
	
	@Embedded private ResellerProductSale productSale = new ResellerProductSale();
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	public static final String COLUMN_PREFIX = "taking_";
	
}
