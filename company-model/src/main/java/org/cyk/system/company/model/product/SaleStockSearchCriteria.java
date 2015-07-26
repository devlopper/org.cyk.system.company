package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.cyk.system.root.model.search.DefaultSearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private BigDecimal minimumQuantity = BigDecimal.ZERO;
	
	public SaleStockSearchCriteria(){
		this(null,null,BigDecimal.ZERO);
	}
	
	public SaleStockSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ZERO);
	}
	
	public SaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		super(fromDate,toDate);
		this.minimumQuantity = minimumQuantity;
	}
	
}
