package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.utility.common.annotation.FieldOverride;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<CashRegisterMovement,CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
		
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable;
	}
	
	@Override
	protected CashRegister getCollection(CashRegisterMovement cashRegisterMovement) {
		return cashRegisterMovement.getCashRegister();
	}
	
	@Override
	protected MovementCollection getMovementCollection(CashRegister cashRegister) {
		return cashRegister.getMovementCollection();
	}
	
	@Override
	protected void selectCollection(CashRegister cashRegister) {
		if(Crud.CREATE.equals(crud))
			inject(CashRegisterMovementBusiness.class).setCashRegister(identifiable, cashRegister);
		super.selectCollection(cashRegister);
	}
		
	@Override
	protected CashRegisterMovement instanciateIdentifiable(CashRegister cashRegister) {
		return inject(CashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount(),cashRegister);
	}
	
	@Getter @Setter @FieldOverride(name=AbstractCashRegisterMovementForm.FIELD_COLLECTION,type=CashRegister.class)
	public static class Form extends AbstractCashRegisterMovementForm<CashRegisterMovement,CashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable;
		}

		@Override
		protected CashRegister getCashRegister() {
			return collection;
		}
		
	}

}
