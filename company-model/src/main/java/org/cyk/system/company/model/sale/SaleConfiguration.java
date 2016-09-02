package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.system.root.model.generator.StringGenerator;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

@Getter @Setter @Embeddable
public class SaleConfiguration extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 2700928054823690772L;

	/**
	 * Zero means no taxes are collected
	 */
	@Column(precision=PERCENT_PRECISION,scale=PERCENT_SCALE,nullable=false) @NotNull
	private BigDecimal valueAddedTaxRate = BigDecimal.ZERO;
	
	@Column(nullable=false) @NotNull private Boolean valueAddedTaxIncludedInCost = Boolean.TRUE;
	
	@OneToOne private StringGenerator identifierGenerator;

	@OneToOne private StringGenerator cashRegisterMovementIdentifierGenerator;
	
	@OneToOne private FiniteStateMachine finiteStateMachine;
	
	@OneToOne private FiniteStateMachine salableProductInstanceCashRegisterFiniteStateMachine;
	
	@OneToOne private FiniteStateMachineState salableProductInstanceCashRegisterSaleConsumeState;
	
	@Column(nullable=false) @NotNull private Boolean balanceMustBeZero = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean balanceCanBeNegative = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean balanceCanBeGreaterThanCost = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean salableProductInstanceAssignableToManyCashRegister = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean allowOnlySalableProductInstanceOfCashRegister = Boolean.FALSE;
	
	private Long minimalNumberOfProductBySale;
	private Long maximalNumberOfProductBySale;
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,valueAddedTaxRate,valueAddedTaxIncludedInCost);
	}
	
	private static final String LOG_FORMAT = SaleConfiguration.class.getSimpleName()+"(VAT_RATE=%s IN_COST=%s)";

}
