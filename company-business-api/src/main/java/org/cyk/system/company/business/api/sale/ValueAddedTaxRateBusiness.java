package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;

import org.cyk.system.company.model.sale.ValueAddedTaxRate;
import org.cyk.system.root.business.api.AbstractEnumerationBusiness;

public interface ValueAddedTaxRateBusiness extends AbstractEnumerationBusiness<ValueAddedTaxRate> {

	BigDecimal computeValueAddedTax(ValueAddedTaxRate valueAddedTaxRate,BigDecimal value);
	
}
