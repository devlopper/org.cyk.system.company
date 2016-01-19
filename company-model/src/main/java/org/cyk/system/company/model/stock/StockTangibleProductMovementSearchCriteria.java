package org.cyk.system.company.model.stock;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;

@Getter @Setter
public class StockTangibleProductMovementSearchCriteria extends AbstractPeriodSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	
	public StockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate) {
		super(fromDate,toDate);
	}
	
}
