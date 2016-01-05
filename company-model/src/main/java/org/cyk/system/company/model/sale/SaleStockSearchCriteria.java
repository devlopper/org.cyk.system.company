package org.cyk.system.company.model.sale;

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
	
	public SaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity, Boolean saleDone) {
		super(fromDate,toDate,minimumQuantity,saleDone);
	}
	
	public SaleStockSearchCriteria(Date fromDate, Date toDate, Boolean saleDone) {
		super(fromDate, toDate, saleDone);
	}
	
}
