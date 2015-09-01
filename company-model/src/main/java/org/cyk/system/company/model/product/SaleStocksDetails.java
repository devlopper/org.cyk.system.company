package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStocksDetails implements Serializable {
	private static final long serialVersionUID = 2804518082667973573L;
	
	private SalesDetails salesDetails = new SalesDetails();
	
	private BigDecimal in, out, remaining;
	
}