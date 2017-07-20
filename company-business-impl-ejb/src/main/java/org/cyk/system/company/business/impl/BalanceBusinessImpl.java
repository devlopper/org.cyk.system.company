package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.BalanceBusiness;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;

public class BalanceBusinessImpl implements BalanceBusiness, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public BigDecimal compute(BigDecimal value, BigDecimal amount) {
		return value.subtract(amount);
	}

	@Override
	public BigDecimal compute(BigDecimal current, BigDecimal amount, BigDecimal previous) {
		return compute(previous == null ? current : previous, amount);
	}

	@Override
	public BigDecimal compute(Cost cost, BigDecimal amount) {
		return compute(cost.getValue(), amount);
	}

	@Override
	public BigDecimal compute(Balance balance, BigDecimal amount) {
		return compute(balance.getValue(), amount);
	}
	
}
