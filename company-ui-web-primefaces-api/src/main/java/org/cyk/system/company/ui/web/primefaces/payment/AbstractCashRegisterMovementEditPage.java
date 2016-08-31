package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractCashRegisterMovementEditPage<MOVEMENT extends AbstractIdentifiable> extends AbstractMovementEditPage<MOVEMENT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		form.getControlSetListeners().add(new ControlSetAdapter<Object>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean build(Object data,Field field) {
				if(field.getName().equals(AbstractCashRegisterMovementForm.FIELD_CASH_REGISTER))
					return Boolean.TRUE.equals(showCashRegisterField());
				return super.build(data,field);
			}
		});
	}
	
	protected abstract CashRegisterMovement getCashRegisterMovement();
	
	@Override
	protected Movement getMovement() {
		return getCashRegisterMovement().getMovement();
	}
	
	@Override
	protected Boolean showCollectionField() {
		return Boolean.FALSE;
	}
	
	protected Boolean showCashRegisterField(){
		return Boolean.TRUE;
	}
		
	@Getter @Setter
	public static abstract class AbstractCashRegisterMovementForm<MOVEMENT extends AbstractIdentifiable> extends AbstractMovementForm<MOVEMENT> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		//@Sequence(direction=Direction.BEFORE,field=AbstractMovementForm.FIELD_CURRENT_TOTAL)
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull protected CashRegister cashRegister;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull protected Movement movement;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegisterMovementMode mode;
		@Input @InputText private String code;
		
		protected abstract CashRegisterMovement getCashRegisterMovement();
		
		@Override
		public void read() {
			super.read();
			cashRegister = getCashRegisterMovement().getCashRegister();
			movement = getCashRegisterMovement().getMovement();
			mode = getCashRegisterMovement().getMode();
			code = getCashRegisterMovement().getCode();
		}
		
		@Override
		public void write() {
			collection = cashRegister.getMovementCollection();
			value = movement.getValue();
			super.write();
			getCashRegisterMovement().setCashRegister(cashRegister);
			getCashRegisterMovement().setMovement(movement);
			getCashRegisterMovement().setMode(mode);
			getCashRegisterMovement().setCode(code);
			
			
		}
		
		@Override
		protected Movement getMovement() {
			return getCashRegisterMovement().getMovement();
		}
		
		/**/
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_MOVEMENT = "movement";
		public static final String FIELD_MODE = "mode";
		public static final String FIELD_CODE = "code";
	}

}
