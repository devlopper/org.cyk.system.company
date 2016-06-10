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
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.generator.StringGenerator;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;

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
	
	@OneToOne @NotNull private FiniteStateMachine finiteStateMachine;
	
	@OneToOne @NotNull private FiniteStateMachine salableProductInstanceCashRegisterFiniteStateMachine;
	
	@OneToOne private ReportTemplate saleReportTemplate;
	
	@OneToOne private ReportTemplate saleCashRegisterMovementReportTemplate;
	
	@OneToOne private ReportTemplate saleAndSaleCashRegisterMovementReportTemplate;
	
	@Column(nullable=false) @NotNull private Boolean balanceMustBeZero = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean balanceCanBeNegative = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean balanceCanBeGreaterThanCost = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean salableProductInstanceAssignableToManyCashRegister = Boolean.FALSE;
	
	@Column(nullable=false) @NotNull private Boolean allowOnlySalableProductInstanceOfCashRegister = Boolean.FALSE;
	
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
