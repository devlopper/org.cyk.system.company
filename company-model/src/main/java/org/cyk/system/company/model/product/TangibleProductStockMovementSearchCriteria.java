package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;

@Getter @Setter
public class TangibleProductStockMovementSearchCriteria extends AbstractPeriodSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	
	public TangibleProductStockMovementSearchCriteria(Date fromDate,Date toDate) {
		super(fromDate,toDate);
	}
	
}
