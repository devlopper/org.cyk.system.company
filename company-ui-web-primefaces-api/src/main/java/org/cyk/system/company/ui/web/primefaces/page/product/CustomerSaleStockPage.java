package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
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
import org.cyk.ui.api.model.table.Cell;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.utility.common.model.table.TableAdapter;

@Named @ViewScoped @Getter @Setter
public class CustomerSaleStockPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CustomerBalanceReportTableDetails> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("company.command.customer.salestock");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CustomerBalanceReportTableDetails> details = new ArrayList<>();
		for(Customer customer : customerBusiness.findAll())
			details.add(new CustomerBalanceReportTableDetails(customer));
		
		TableAdapter<Row<CustomerBalanceReportTableDetails>, Column, CustomerBalanceReportTableDetails, String, Cell, String> listener;
		listener = new TableAdapter<Row<CustomerBalanceReportTableDetails>, Column, CustomerBalanceReportTableDetails, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return CompanyBusinessLayer.getInstance().reportCustomerSaleStockFieldIgnored(field);
			}
		};
		
		table = createDetailsTable(CustomerBalanceReportTableDetails.class, details,listener, "");	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
				CompanyBusinessLayer.getInstance().getParameterCustomerReportSaleStock());
	}
	
}