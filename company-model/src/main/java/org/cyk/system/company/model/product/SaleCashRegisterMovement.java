package org.cyk.system.company.model.product;

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

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class SaleCashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Sale sale;
	@OneToOne @NotNull private CashRegisterMovement cashRegisterMovement;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) private BigDecimal amountIn = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) private BigDecimal amountOut = BigDecimal.ZERO;
	
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
	
	

}
