package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SaleCashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Sale sale;
	@OneToOne @NotNull private CashRegisterMovement cashRegisterMovement;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountIn = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountOut = BigDecimal.ZERO;
	
	@Embedded private Balance balance = new Balance();

	@OneToOne private File report;
	
	public SaleCashRegisterMovement(Sale sale,CashRegisterMovement cashRegisterMovement) {
		super();
		this.sale = sale;
		this.cashRegisterMovement = cashRegisterMovement;
	}

	public SaleCashRegisterMovement(Sale sale, CashRegisterMovement cashRegisterMovement, BigDecimal amountIn,BigDecimal amountOut) {
		super();
		this.sale = sale;
		this.cashRegisterMovement = cashRegisterMovement;
		this.amountIn = amountIn;
		this.amountOut = amountOut;
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,sale==null?Constant.EMPTY_STRING:sale.getIdentifier(),amountIn,amountOut,cashRegisterMovement.getLogMessage(),balance.getLogMessage());
	}
	
	/**/
	
	private static final String LOG_FORMAT = SaleCashRegisterMovement.class.getSimpleName()+"(S=%s I=%s O=%s %s %s)";
	
	public static final String FIELD_SALE = "sale";
	public static final String FIELD_CASH_REGISTER_MOVEMENT = "cashRegisterMovement";
	public static final String FIELD_AMOUNT_IN = "amountIn";
	public static final String FIELD_AMOUNT_OUT = "amountOut";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_REPORT = "report";

}
