package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.stock.StockableTangibleProductDetails;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class StockableTangibleProductConsultPage extends AbstractConsultPage<StockableTangibleProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<StockableTangibleProductDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(StockableTangibleProductDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<StockableTangibleProduct,StockableTangibleProductDetails>(StockableTangibleProduct.class, StockableTangibleProductDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
		
		
	}

}