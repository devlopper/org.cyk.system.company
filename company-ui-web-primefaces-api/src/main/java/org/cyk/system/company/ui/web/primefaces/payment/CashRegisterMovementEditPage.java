package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<CashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable;
	}
		
	@Override
	protected CashRegisterMovement instanciateIdentifiable() {
		return CompanyBusinessLayer.getInstance().getCashRegisterMovementBusiness().instanciateOne((Person) userSession.getUser());
	}
	
	@Getter @Setter
	public static class Form extends AbstractCashRegisterMovementForm<CashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable;
		}
		
	}
	
	public static class Adapter extends AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.Adapter.Default<CashRegisterMovement> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public Adapter() {
			super(CashRegisterMovement.class);
			FormConfiguration configuration = createFormConfiguration(Crud.CREATE, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			//configuration.addRequiredFieldNames(Form.FIELD_CODE,Form.FIELD_NAME);
			
			configuration = createFormConfiguration(Crud.UPDATE, UIManager.getInstance().businessEntityInfos(entityTypeClass).getUserInterface().getLabelId());
			//configuration.addRequiredFieldNames(Form.FIELD_CODE,Form.FIELD_NAME);
			
		}
		
	}

}
