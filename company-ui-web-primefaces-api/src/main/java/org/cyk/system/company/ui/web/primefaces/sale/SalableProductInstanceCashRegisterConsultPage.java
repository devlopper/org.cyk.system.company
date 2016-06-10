package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterDetails;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterConsultPage extends AbstractConsultPage<SalableProductInstanceCashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<SalableProductInstanceCashRegisterDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(SalableProductInstanceCashRegisterDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<SalableProductInstanceCashRegister,SalableProductInstanceCashRegisterDetails>(SalableProductInstanceCashRegister.class, SalableProductInstanceCashRegisterDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
					
	}

}
