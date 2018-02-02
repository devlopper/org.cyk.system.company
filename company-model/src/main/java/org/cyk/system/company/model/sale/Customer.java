package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity 
public class Customer extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = 1371797411549893368L;
	
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal turnover;
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal saleCount;
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal balance;
	
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal paid;
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal paymentCount;
	
	//TODO should stock attributes but not only specific to sale stock concept
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal saleStockInputCount;
	@Column(precision=10,scale=FLOAT_SCALE) @NotNull private BigDecimal saleStockOutputCount;

	/**/
	
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_SALE_COUNT = "saleCount";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_PAID = "paid";
	public static final String FIELD_PAYMENT_COUNT = "paymentCount";
	
	/**/
	
}
