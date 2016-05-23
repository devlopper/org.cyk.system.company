package org.cyk.system.company.model.payment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class CashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(unique=true,nullable=true) private String computedIdentifier;
	@ManyToOne @NotNull private CashRegister cashRegister;
	@ManyToOne @NotNull private CashRegisterMovementMode mode;
	@ManyToOne @NotNull private Movement movement;
	
	public CashRegisterMovement(CashRegister cashRegister,Movement movement) {
		super();
		this.cashRegister = cashRegister;
		this.movement = movement;
	}
	
	/**/
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT, computedIdentifier,cashRegister.getCode(),movement.getValue());
	}
	
	private static final String LOG_FORMAT = CashRegisterMovement.class.getSimpleName()+"(I=%S C=%s M=%s)";
	
	/**/
	
	public static final String FIELD_COMPUTED_IDENTIFIER = "computedIdentifier";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
		
}
