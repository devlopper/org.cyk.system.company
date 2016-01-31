package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterConsultPage extends AbstractConsultPage<CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CashRegisterDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(CashRegisterDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<CashRegister,CashRegisterDetails>(CashRegister.class, CashRegisterDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
		
	}

}
