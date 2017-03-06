package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.model.search.BigDecimalSearchCriteria;
import org.cyk.system.root.model.search.StringSearchCriteria;

@Getter @Setter
public class StockTangibleProductMovementSearchCriteria extends AbstractPeriodSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private BigDecimalSearchCriteria minimumQuantitySearchCriteria;
	
	public StockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		super(fromDate,toDate);
		this.minimumQuantitySearchCriteria=new BigDecimalSearchCriteria(minimumQuantity);
	}

	public StockTangibleProductMovementSearchCriteria(StockTangibleProductMovementSearchCriteria stockTangibleProductMovementSearchCriteria) {
		super(stockTangibleProductMovementSearchCriteria);
		this.minimumQuantitySearchCriteria = new BigDecimalSearchCriteria(stockTangibleProductMovementSearchCriteria.minimumQuantitySearchCriteria);
	}
	
	/**/
	
	public static final String FIELD_MINIMUM_QUANTITY = "minimumQuantity";

	@Override
	public void set(String arg0) {
		
	}

	@Override
	public void set(StringSearchCriteria arg0) {
		
	}
	
}
