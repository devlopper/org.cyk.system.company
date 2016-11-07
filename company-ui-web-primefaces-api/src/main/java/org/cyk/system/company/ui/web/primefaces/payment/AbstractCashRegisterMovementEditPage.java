package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter
public abstract class AbstractCashRegisterMovementEditPage<ITEM extends AbstractIdentifiable,COLLECTION extends AbstractIdentifiable> extends AbstractMovementEditPage<ITEM,COLLECTION> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	protected abstract CashRegisterMovement getCashRegisterMovement();
	
	@Override
	protected Movement getMovement() {
		return getCashRegisterMovement().getMovement();
	}
	
	@Override
	protected void selectCollection(COLLECTION collection) {
		super.selectCollection(collection);
		((AbstractCashRegisterMovementForm<?,?>)form.getData()).setMovement(getMovement());
	}
	
	@Override
	protected Boolean showCollectionField() {
		return Boolean.TRUE;
	}
	
	protected Boolean showCashRegisterField(){
		return Boolean.TRUE;
	}
		
	@Getter @Setter
	public static abstract class AbstractCashRegisterMovementForm<ITEM extends AbstractIdentifiable,COLLECTION extends AbstractIdentifiable> extends AbstractMovementForm<ITEM,COLLECTION> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull protected Movement movement;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull protected CashRegisterMovementMode mode;
		
		protected abstract CashRegister getCashRegister();
		protected abstract CashRegisterMovement getCashRegisterMovement();
		
		@Override
		public void read() {
			super.read();
			movement = getCashRegisterMovement().getMovement();
			mode = getCashRegisterMovement().getMode();
			code = getCashRegisterMovement().getCode();
		}
		
		@Override
		public void write() {
			super.write();
			getCashRegisterMovement().setCashRegister(getCashRegister());
			getCashRegisterMovement().setMovement(movement);
			getCashRegisterMovement().setMode(mode);
			getCashRegisterMovement().setCode(code);
		}
		
		@Override
		protected Movement getMovement() {
			return getCashRegisterMovement().getMovement();
		}
		
		/**/
		public static final String FIELD_MOVEMENT = "movement";
		public static final String FIELD_MODE = "mode";
		public static final String FIELD_CODE = "code";
	}

}
