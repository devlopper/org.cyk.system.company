package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementConsultPage extends AbstractConsultPage<CashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CashRegisterMovementDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(CashRegisterMovementDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<CashRegisterMovement,CashRegisterMovementDetails>(CashRegisterMovement.class, CashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
		
	}

}
