package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class SalableProduct extends AbstractCollection<SalableProductInstance> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Product product;
	
	/**
	 * The unit price of the product. null means the price will be determine at runtime
	 */
	@Column(precision=10,scale=FLOAT_SCALE) private BigDecimal price;
	
	public SalableProduct(Product product, BigDecimal price) {
		super(product.getCode(),product.getName(),product.getAbbreviation(),product.getDescription());
		this.product = product;
		this.price = price;
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,product.getCode(),price);
	}
	
	/**/
	
	private static final String LOG_FORMAT = SalableProduct.class.getSimpleName()+"(Code=%s Price=%s)";

	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_PRICE = "price";
}
