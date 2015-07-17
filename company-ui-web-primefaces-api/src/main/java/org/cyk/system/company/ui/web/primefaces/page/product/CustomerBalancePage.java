package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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
public class CustomerBalancePage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CustomerBalanceReportTableDetails> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CustomerBalanceReportTableDetails> details = new ArrayList<>();
		String balanceType = requestParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType());
		final Boolean all = CompanyBusinessLayer.getInstance().getParameterCustomerBalanceAll().equals(balanceType);
		contentTitle = all?text("company.command.customer.balance"):text("field.credence");
		Collection<Customer> customers = all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
		for(Customer customer : customers)
			details.add(new CustomerBalanceReportTableDetails(customer));
		TableAdapter<Row<CustomerBalanceReportTableDetails>, Column, CustomerBalanceReportTableDetails, String, Cell, String> listener;
		listener = new TableAdapter<Row<CustomerBalanceReportTableDetails>, Column, CustomerBalanceReportTableDetails, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return all?CompanyBusinessLayer.getInstance().reportCustomerBalanceFieldIgnored(field):
					CompanyBusinessLayer.getInstance().reportCustomerCredenceFieldIgnored(field);
			}
		};
		table = createDetailsTable(CustomerBalanceReportTableDetails.class, details, listener, "");	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
				CompanyBusinessLayer.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), balanceType);
	}
	
}