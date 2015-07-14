package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.product.CustomerBalanceReportTableDetails;
import org.cyk.system.company.model.product.Customer;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class CustomerSaleStockPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CustomerBalanceReportTableDetails> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("field.credence");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CustomerBalanceReportTableDetails> details = new ArrayList<>();
		for(Customer customer : customerBusiness.findAll())
			details.add(new CustomerBalanceReportTableDetails(customer));
		table = createDetailsTable(CustomerBalanceReportTableDetails.class, details, "");	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
				CompanyBusinessLayer.getInstance().getParameterCustomerReportSaleStock());
	}
	
}