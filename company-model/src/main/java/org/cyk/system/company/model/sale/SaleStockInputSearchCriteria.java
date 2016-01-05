package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockInputSearchCriteria extends AbstractSaleStockSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private BigDecimal minimumRemainingGoodsCount = BigDecimal.ZERO;
	
	public SaleStockInputSearchCriteria(){
		this(null,null,BigDecimal.ZERO);
	}
	
	public SaleStockInputSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ZERO);
	}
	
	public SaleStockInputSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumRemainingGoodsCount) {
		super(fromDate,toDate,BigDecimal.ZERO);
		this.minimumRemainingGoodsCount = minimumRemainingGoodsCount;
	}

	public SaleStockInputSearchCriteria(Date fromDate, Date toDate, Boolean saleDone) {
		super(fromDate, toDate, saleDone);
	}
	
	
}
