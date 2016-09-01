package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor
public class SaleConfigurationReport extends AbstractGeneratable<SaleConfigurationReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String valueAddedTaxRate;
	private Boolean valueAddedTaxIncludedInCost = Boolean.TRUE;
	private Boolean balanceMustBeZero = Boolean.FALSE;
	private Boolean balanceCanBeNegative = Boolean.FALSE;
	private Boolean balanceCanBeGreaterThanCost = Boolean.FALSE;
	private Boolean salableProductInstanceAssignableToManyCashRegister = Boolean.FALSE;
	private Boolean allowOnlySalableProductInstanceOfCashRegister = Boolean.FALSE;
	private String minimalNumberOfProductBySale;
	private String maximalNumberOfProductBySale;
	
	@Override
	public void generate() {
		valueAddedTaxRate="18%";
		minimalNumberOfProductBySale=provider.randomInt(1, 100)+"";
		maximalNumberOfProductBySale=provider.randomInt(1, 100)+"";
	}
	
}
