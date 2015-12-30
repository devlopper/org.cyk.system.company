package org.cyk.system.company.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class CashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(unique=true,nullable=true) private String computedIdentifier;
	@ManyToOne @NotNull private CashRegister cashRegister;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amount;
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull private Date date;
	
	public CashRegisterMovement(CashRegister cashRegister) {
		super();
		this.cashRegister = cashRegister;
	}
	
	public static final String FIELD_COMPUTED_IDENTIFIER = "computedIdentifier";
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
		
}
