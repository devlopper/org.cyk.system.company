package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.ValueAddedTaxRateBusiness;
import org.cyk.system.company.model.sale.ValueAddedTaxRate;
import org.cyk.system.company.persistence.api.sale.ValueAddedTaxRateDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.utility.common.helper.LoggingHelper;

public class ValueAddedTaxRateBusinessImpl extends AbstractEnumerationBusinessImpl<ValueAddedTaxRate, ValueAddedTaxRateDao> implements ValueAddedTaxRateBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ValueAddedTaxRateBusinessImpl(ValueAddedTaxRateDao dao) {
		super(dao);
	}
	
	@Override
	public BigDecimal computeValueAddedTax(ValueAddedTaxRate valueAddedTaxRate, BigDecimal amount) {
		LoggingHelper.Message.Builder loggingMessageBuilder = new LoggingHelper.Message.Builder.Adapter.Default();
		loggingMessageBuilder.addManyParameters("compute value added tax");
		BigDecimal vat;
		Boolean included = Boolean.TRUE.equals(valueAddedTaxRate.getIncluded());
		loggingMessageBuilder.addNamedParameters("amount",amount,"included",included,"rate",valueAddedTaxRate.getValue());
		if(included)
			vat = amount.subtract(amount.divide(BigDecimal.ONE.add(valueAddedTaxRate.getValue()),RoundingMode.DOWN));
		else
			vat = valueAddedTaxRate.getValue().multiply(amount);
		loggingMessageBuilder.addNamedParameters("result",vat);
		logTrace(loggingMessageBuilder);
		return vat;
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<ValueAddedTaxRate> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(ValueAddedTaxRate.class);
			addParameterArrayElementStringIndexInstance(2,ValueAddedTaxRate.FIELD_VALUE);
		}
		
	}	
}
