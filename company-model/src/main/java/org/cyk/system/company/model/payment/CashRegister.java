package org.cyk.system.company.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class CashRegister extends AbstractEnumeration implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal balance = BigDecimal.ZERO;

	@Column(precision=10,scale=FLOAT_SCALE)
	@Input
	@InputNumber
	private BigDecimal minimumBalance;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	@Input
	@InputNumber
	private BigDecimal maximumBalance;
}
