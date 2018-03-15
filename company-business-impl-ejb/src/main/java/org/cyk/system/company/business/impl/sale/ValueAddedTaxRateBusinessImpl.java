package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.ValueAddedTaxRateBusiness;
import org.cyk.system.company.model.sale.ValueAddedTaxRate;
import org.cyk.system.company.persistence.api.sale.ValueAddedTaxRateDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public class ValueAddedTaxRateBusinessImpl extends AbstractEnumerationBusinessImpl<ValueAddedTaxRate, ValueAddedTaxRateDao> implements ValueAddedTaxRateBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ValueAddedTaxRateBusinessImpl(ValueAddedTaxRateDao dao) {
		super(dao);
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
