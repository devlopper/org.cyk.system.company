package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStocksDetails implements Serializable {
	private static final long serialVersionUID = 2804518082667973573L;
	
	private SaleResults saleResults = new SaleResults();
	
	private BigDecimal in, out, remaining;
	
}