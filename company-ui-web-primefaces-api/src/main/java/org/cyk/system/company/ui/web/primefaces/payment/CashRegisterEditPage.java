package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementCollectionEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterEditPage extends AbstractMovementCollectionEditPage<CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected MovementCollection getMovementCollection() {
		return identifiable.getMovementCollection();
	}
	
	@Override
	protected CashRegister instanciateIdentifiable() {
		CashRegister cashRegister = super.instanciateIdentifiable();
		cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaultOwnedCompany());
		return cashRegister;
	}
	
	@Getter @Setter
	public static class Form extends AbstractMovementCollectionForm<CashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo private MovementCollection movementCollection;
		
		@Override
		protected MovementCollection getMovementCollection() {
			return identifiable.getMovementCollection();
		}
		
		@Override
		public void read() {
			super.read();
			movementCollection = getMovementCollection();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.setMovementCollection(movementCollection);
		}
		
		public static final String FIELD_MOVEMENT_COLLECTION = "movementCollection";
		
	}
	
}
