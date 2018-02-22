package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Embeddable
public class SaleConfiguration extends AbstractModelElement implements Serializable {
	private static final long serialVersionUID = 2700928054823690772L;

	/**
	 * Zero means no taxes are collected
	 */
	@Column(precision=PERCENT_PRECISION,scale=PERCENT_SCALE) private BigDecimal valueAddedTaxRate = BigDecimal.ZERO;
	@Column private Boolean valueAddedTaxIncludedInCost = Boolean.TRUE;
	
	/*
	 * Use join to attach a string generator to the desired identifiable
	@OneToOne private StringGenerator identifierGenerator;//TODO use a code in constant instead of declaring this here
	@OneToOne private StringGenerator cashRegisterMovementIdentifierGenerator;//TODO use a code in constant instead of declaring this here
	*/
	
	/*
	 * Use join to attach a finite state machine and state to the desired identifiable
	@OneToOne private FiniteStateMachine finiteStateMachine;
	@OneToOne private FiniteStateMachine salableProductInstanceCashRegisterFiniteStateMachine;
	@OneToOne private FiniteStateMachineState salableProductInstanceCashRegisterSaleConsumeState;
	*/
	
	@Column private Boolean balanceMustBeZero = Boolean.FALSE;
	@Column private Boolean balanceCanBeNegative = Boolean.FALSE;
	@Column private Boolean balanceCanBeGreaterThanCost = Boolean.FALSE;
	@Column private Boolean salableProductInstanceAssignableToManyCashRegister = Boolean.FALSE;	
	@Column private Boolean allowOnlySalableProductInstanceOfCashRegister = Boolean.FALSE;
	
	private Long minimalNumberOfProductBySale;
	private Long maximalNumberOfProductBySale;
	
	public static final String FIELD_VALUE_ADDED_TAX_RATE = "valueAddedTaxRate";
	
}
