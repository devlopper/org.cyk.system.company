package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockOutputSearchCriteria extends AbstractPeriodSearchCriteria implements Serializable {

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
}
