package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class CustomerSalableProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Customer customer;
	@OneToOne @NotNull private SalableProduct salableProduct;
	@Column(precision=10,scale=FLOAT_SCALE) private BigDecimal price;

	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,customer.getLogMessage(),salableProduct.getLogMessage());
	}
	
	/**/
	
	private static final String LOG_FORMAT = CustomerSalableProduct.class.getSimpleName()+"(%s %s)";
	
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
	public static final String FIELD_PRICE = "price";


}
