package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.party.person.AbstractActor;

@Getter @Setter @NoArgsConstructor @Entity 
public class Customer extends AbstractActor implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal turnover = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal saleCount = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal balance = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal paid = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal paymentCount = BigDecimal.ZERO;
	
	//TODO should stock attributes but not only specific to sale stock concept
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal saleStockInputCount = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal saleStockOutputCount = BigDecimal.ZERO;

	/**/
	
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_SALE_COUNT = "saleCount";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_PAID = "paid";
	public static final String FIELD_PAYMENT_COUNT = "paymentCount";
	
	/**/
	
	public static class SearchCriteria extends AbstractSearchCriteria<Customer> {

		private static final long serialVersionUID = -7909506438091294611L;

		public SearchCriteria() {
			this(null);
		}

		public SearchCriteria(String name) {
			super(name);
		}
		
		
	}
}
