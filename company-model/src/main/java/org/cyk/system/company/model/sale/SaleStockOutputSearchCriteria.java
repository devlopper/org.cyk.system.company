package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockOutputSearchCriteria extends AbstractSaleStockSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private BigDecimal minimumPaid;
	
	public SaleStockOutputSearchCriteria(){
		this(null,null,BigDecimal.ONE /*LOWEST_NON_ZERO_POSITIVE_VALUE*/);
	}
	
	public SaleStockOutputSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumPaid) {
		super(fromDate,toDate);
		this.minimumPaid = minimumPaid;
	}
	
	public SaleStockOutputSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ONE/*LOWEST_NON_ZERO_POSITIVE_VALUE*/);
	}
	
	public SaleStockOutputSearchCriteria(Date fromDate, Date toDate, Boolean saleDone) {
		super(fromDate, toDate, saleDone);
		this.minimumPaid = BigDecimal.ONE;
	}
}
