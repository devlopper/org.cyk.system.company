package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ResellerProduct extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private Reseller reseller;
	@ManyToOne @NotNull private Product product;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal takingUnitPrice;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal saleUnitPrice;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commissionRate = BigDecimal.ZERO;
	
	public ResellerProduct(Reseller reseller, Product product) {
		super();
		this.reseller = reseller;
		this.product = product;
	}
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	/**/
	
	public static final String FIELD_RESELLER = "reseller";
	public static final String FIELD_PRODUCT = "product";
}
