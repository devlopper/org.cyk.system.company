package org.cyk.system.company.model.payment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Interval;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Deprecated
public class CashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private CashRegister cashRegister;
	@ManyToOne @NotNull private Movement movement;
	@ManyToOne @NotNull private CashRegisterMovementMode mode;
	@ManyToOne private Interval stampDutyInterval;
	
	public CashRegisterMovement(CashRegister cashRegister,Movement movement) {
		super();
		this.cashRegister = cashRegister;
		this.movement = movement;
	}
	
	/**/
	
	/**/
	
	public static final String FIELD_MODE = "mode";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
		
}
