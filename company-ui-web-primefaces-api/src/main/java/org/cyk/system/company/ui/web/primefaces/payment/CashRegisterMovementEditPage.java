package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<CashRegisterMovement,CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable;
	}
		
	/*@Override
	protected CashRegisterMovement instanciateIdentifiable() {
		return inject(CashRegisterMovementBusiness.class).instanciateOne((Person) userSession.getUser());
	}*/
	
	@Getter @Setter
	public static class Form extends AbstractCashRegisterMovementForm<CashRegisterMovement,CashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable;
		}

		@Override
		protected CashRegister getCashRegister() {
			return getCashRegisterMovement().getCashRegister();
		}
		
	}

}
