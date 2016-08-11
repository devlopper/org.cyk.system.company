package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormManyPage;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementListPage extends AbstractCrudManyPage<CashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
	public static class Adapter extends AbstractBusinessEntityFormManyPage.BusinessEntityFormManyPageListener.Adapter.Default<CashRegisterMovement> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public Adapter() {
			super(CashRegisterMovement.class);
			FormConfiguration configuration = createFormConfiguration(Crud.READ, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			configuration.addFieldNames(MovementDetails.FIELD_DATE,CashRegisterMovementDetails.FIELD_COMPUTED_IDENTIFIER,CashRegisterMovementDetails.FIELD_CASH_REGISTER
					,MovementDetails.FIELD_VALUE,CashRegisterMovementDetails.FIELD_MOVEMENT);
		}
		
	}
	
}
