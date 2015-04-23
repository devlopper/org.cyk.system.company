package org.cyk.system.company.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class CashRegister extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	@Input
	@InputText
	@Column(nullable=false,unique=true)
	@NotNull(groups=org.cyk.utility.common.validation.Client.class)
	protected String code;
	
	@ManyToOne @NotNull
	@Input
	@InputChoice
	@InputOneChoice
	@InputOneCombo
	private OwnedCompany ownedCompany;
	
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
	
	@Override
	public String getUiString() {
		return code;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
}
