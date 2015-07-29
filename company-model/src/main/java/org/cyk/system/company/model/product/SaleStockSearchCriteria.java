package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockSearchCriteria extends AbstractSaleStockSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	public SaleStockSearchCriteria(){
		super();
	}
	
	public SaleStockSearchCriteria(Date fromDate,Date toDate) {
		super(fromDate,toDate);
	}
	
	public SaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		super(fromDate,toDate,minimumQuantity);
	}
	
}
