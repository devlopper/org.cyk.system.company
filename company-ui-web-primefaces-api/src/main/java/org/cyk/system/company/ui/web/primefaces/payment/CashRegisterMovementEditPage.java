package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.impl.BusinessInterfaceLocator;
import org.cyk.utility.common.annotation.FieldOverride;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<CashRegisterMovement,CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		setFieldValue(AbstractMovementForm.FIELD_COLLECTION, getCashRegisterMovement().getCashRegister());
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable;
	}
		
	@Override
	protected CashRegisterMovement instanciateIdentifiable() {
		CashRegisterMovement identifiable;
		Long collectionIdentifier = requestParameterLong(CashRegister.class);
		if(collectionIdentifier==null){
			return identifiable = super.instanciateIdentifiable();
		}else{
			CashRegister collection = inject(BusinessInterfaceLocator.class).injectTyped(CashRegister.class).find(collectionIdentifier);
			identifiable = inject(CashRegisterMovementBusiness.class).instanciateOne(collection);
		}
		return identifiable;
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
			return getCashRegisterMovement().getCashRegister();
		}
		
		@Override
		public void write() {
			super.write();
		}
		
	}

}
