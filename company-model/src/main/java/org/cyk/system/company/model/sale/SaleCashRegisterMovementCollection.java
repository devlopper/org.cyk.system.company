package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SaleCashRegisterMovementCollection extends AbstractCollection<SaleCashRegisterMovement> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_ACCOUNTING_PERIOD) @NotNull private AccountingPeriod accountingPeriod;
	@OneToOne @JoinColumn(name=COLUMN_CASH_REGISTER_MOVEMENT) @NotNull private CashRegisterMovement cashRegisterMovement;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountIn = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountOut = BigDecimal.ZERO;
	
	/**/
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_CASH_REGISTER_MOVEMENT = "cashRegisterMovement";
	public static final String FIELD_AMOUNT_IN = "amountIn";
	public static final String FIELD_AMOUNT_OUT = "amountOut";
	
	/**/
	
	public static final String COLUMN_ACCOUNTING_PERIOD = FIELD_ACCOUNTING_PERIOD;
	public static final String COLUMN_CASH_REGISTER_MOVEMENT = FIELD_CASH_REGISTER_MOVEMENT;
}
