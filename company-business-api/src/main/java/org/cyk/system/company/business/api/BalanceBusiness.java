package org.cyk.system.company.business.api;

import java.math.BigDecimal;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;

public interface BalanceBusiness {
	
	BigDecimal compute(BigDecimal value,BigDecimal amount);
	BigDecimal compute(BigDecimal current,BigDecimal amount,BigDecimal previous);
	
	BigDecimal compute(Cost cost,BigDecimal amount);
	BigDecimal compute(Balance balance,BigDecimal amount);
}
