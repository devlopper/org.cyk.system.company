package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.CustomerReportTableRow;
import org.cyk.system.company.model.product.Customer;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CustomerBalancePage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CustomerReportTableRow> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CustomerReportTableRow> details = new ArrayList<>();
		String balanceType = requestParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType());
		final Boolean all = CompanyReportRepository.getInstance().getParameterCustomerBalanceAll().equals(balanceType);
		contentTitle = all?text("company.command.customer.balance"):text("field.credence");
		Collection<Customer> customers = all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
		for(Customer customer : customers)
			details.add(new CustomerReportTableRow(customer));
		ColumnAdapter listener;
		listener = new ColumnAdapter(){
			@Override
			public Boolean isColumn(Field field) {
				return all?CustomerReportTableRow.balanceFieldIgnored(field):
					CustomerReportTableRow.credenceFieldIgnored(field);
			}
		};
		table = createDetailsTable(CustomerReportTableRow.class, details, listener, "");	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), balanceType);
	}
	
}