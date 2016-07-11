package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class CustomerSalableProductEditPage extends AbstractCrudOnePage<CustomerSalableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
			
	public static class Form extends AbstractFormModel<CustomerSalableProduct> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Customer customer;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SalableProduct salableProduct;
		@Input @InputNumber private BigDecimal price;
		
		/**/
		
		public static final String FIELD_CUSTOMER = "customer";
		public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
		public static final String FIELD_PRICE = "price";
	}

}
