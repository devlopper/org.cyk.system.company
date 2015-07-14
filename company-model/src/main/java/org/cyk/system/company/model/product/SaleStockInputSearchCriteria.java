package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.search.DefaultSearchCriteria;

@Getter @Setter
public class SaleStockInputSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private BigDecimal minimumRemainingGoodsCount = BigDecimal.ZERO;
	
	public SaleStockInputSearchCriteria(){
		this(null,null,null);
	}
	
	public SaleStockInputSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,null);
	}
	
	public SaleStockInputSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumRemainingGoodsCount) {
		super(fromDate,toDate);
		this.minimumRemainingGoodsCount = minimumRemainingGoodsCount;
	}
	
}
