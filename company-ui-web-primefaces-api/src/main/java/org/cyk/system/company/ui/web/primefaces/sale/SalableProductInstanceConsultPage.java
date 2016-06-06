package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.SalableProductInstanceDetails;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceConsultPage extends AbstractConsultPage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<SalableProductInstanceDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(SalableProductInstanceDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<SalableProductInstance,SalableProductInstanceDetails>(SalableProductInstance.class, SalableProductInstanceDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
					
	}

}
