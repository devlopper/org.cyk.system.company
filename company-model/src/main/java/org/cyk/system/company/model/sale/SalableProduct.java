package org.cyk.system.company.model.sale;

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
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.ENUMERATION)
public class SalableProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Input @InputChoice @InputOneChoice @InputOneCombo
	@ManyToOne @NotNull private Product product;
	
	/**
	 * The unit price of the product. null means the price will be determine at runtime
	 */
	@Input @InputNumber
	@Column(precision=10,scale=FLOAT_SCALE) private BigDecimal price;
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,product.getCode(),price);
	}
	
	/**/
	
	private static final String LOG_FORMAT = SalableProduct.class.getSimpleName()+"(C=%s P=%s)";

	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_PRICE = "price";
}
