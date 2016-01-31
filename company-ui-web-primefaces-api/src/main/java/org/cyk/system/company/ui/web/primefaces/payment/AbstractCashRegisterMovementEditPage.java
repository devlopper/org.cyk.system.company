package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractCashRegisterMovementEditPage<MOVEMENT extends AbstractIdentifiable> extends AbstractMovementEditPage<MOVEMENT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	protected abstract CashRegisterMovement getCashRegisterMovement();
	
	@Override
	protected Movement getMovement() {
		return getCashRegisterMovement().getMovement();
	}
	
	@Override
	protected Boolean showCollectionField() {
		return Boolean.FALSE;
	}
		
	@Getter @Setter
	public static abstract class AbstractCashRegisterMovementForm<MOVEMENT extends AbstractIdentifiable> extends AbstractMovementForm<MOVEMENT> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Sequence(direction=Direction.BEFORE,field=AbstractMovementForm.FIELD_ACTION)
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull protected CashRegister cashRegister;
		
		protected abstract CashRegisterMovement getCashRegisterMovement();
		
		@Override
		protected Movement getMovement() {
			return getCashRegisterMovement().getMovement();
		}
		
		/**/
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		
	}

}
