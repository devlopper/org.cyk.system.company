package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.CustomerSalableProductDetails;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class CustomerSalableProductConsultPage extends AbstractConsultPage<CustomerSalableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CustomerSalableProductDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(CustomerSalableProductDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<CustomerSalableProduct,CustomerSalableProductDetails>(CustomerSalableProduct.class, CustomerSalableProductDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
					
	}

}
