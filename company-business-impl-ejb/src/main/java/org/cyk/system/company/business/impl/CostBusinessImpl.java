package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.CostBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.root.business.impl.AbstractBusinessServiceImpl;
import org.cyk.utility.common.helper.ArrayHelper;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.NumberHelper;

public class CostBusinessImpl extends AbstractBusinessServiceImpl implements CostBusiness, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void add(Cost cost, BigDecimal value, BigDecimal numberOfProceedElements, BigDecimal tax,BigDecimal turnover) {
		NumberHelper.getInstance().add(BigDecimal.class, cost, Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, numberOfProceedElements);
		NumberHelper.getInstance().add(BigDecimal.class, cost, Cost.FIELD_VALUE, value);
		NumberHelper.getInstance().add(BigDecimal.class, cost, Cost.FIELD_TAX, tax);
		NumberHelper.getInstance().add(BigDecimal.class, cost, Cost.FIELD_TURNOVER, turnover);
	}
	
	@Override
	public void add(Cost cost, Collection<Cost> costs) {
		if(cost!=null && CollectionHelper.getInstance().isNotEmpty(costs))
			for(Cost index : costs)
				add(cost, index.getValue(), index.getNumberOfProceedElements(), index.getTax(), index.getTurnover());
	}
	
	@Override
	public void add(Cost cost, Cost... costs) {
		if(cost!=null && ArrayHelper.getInstance().isNotEmpty(costs))
			add(cost,Arrays.asList(costs));
	}
	
	
}
