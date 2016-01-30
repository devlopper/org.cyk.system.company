package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractMovementEditPage<CashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected Movement getMovement() {
		return identifiable.getMovement();
	}
	
	@Override
	protected CashRegisterMovement instanciateIdentifiable() {
		return CompanyBusinessLayer.getInstance().getCashRegisterMovementBusiness().instanciate((Person) userSession.getUser());
	}
	
	@Getter @Setter
	public static class Form extends AbstractMovementForm<CashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		
		@Override
		protected Movement getMovement() {
			return identifiable.getMovement();
		}
		
		/**/
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		
	}

}
