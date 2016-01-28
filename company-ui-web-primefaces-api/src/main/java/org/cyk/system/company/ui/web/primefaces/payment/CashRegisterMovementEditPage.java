package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractCrudOnePage<CashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		
		createAjaxBuilder(Form.FIELD_ACTION).updatedFieldNames(Form.FIELD_VALUE)
		.method(MovementAction.class,new ListenValueMethod<MovementAction>() {
			@Override
			public void execute(MovementAction movementAction) {
				selectMovementAction(movementAction);
			}
		}).build();
		
		//((Form)form.getData()).setCollection(identifiable.getCollection());
		selectMovementAction(identifiable.getMovement().getAction());
		
	}
	
	@Override
	protected CashRegisterMovement instanciateIdentifiable() {
		CashRegisterMovement cashRegisterMovement = super.instanciateIdentifiable();
		CashRegister cashRegister = CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson((Person) userSession.getUser()).getCashRegister();
		cashRegisterMovement.setMovement(RootBusinessLayer.getInstance().getMovementBusiness().instanciate(cashRegister.getMovementCollection(), Boolean.TRUE));
	}
	
	private void selectMovementAction(MovementAction movementAction){
		((Form)form.getData()).setValue(null);
	}
	
	@Getter @Setter
	public static class Form extends AbstractFormModel<CashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo @NotNull private MovementAction action;
		@Input @InputNumber @NotNull private BigDecimal value;
		
		@Override
		public void read() {
			super.read();
			if(identifiable.getMovement().getValue()!=null)
				value = identifiable.getMovement().getValue().abs();
		}
		
		@Override
		public void write() {
			super.write();
			if(identifiable.getMovement().getCollection().getDecrementAction().equals(identifiable.getMovement().getAction()))
				identifiable.getMovement().setValue(value.negate());
		}
		
		/**/
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_ACTION = "action";
		public static final String FIELD_VALUE = "value";
	}

}
