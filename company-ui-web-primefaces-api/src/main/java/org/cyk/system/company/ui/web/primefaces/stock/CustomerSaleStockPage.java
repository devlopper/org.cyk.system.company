package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.impl.sale.CustomerReportTableRow;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CustomerSaleStockPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CustomerReportTableRow> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("company.command.customer.salestock");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CustomerReportTableRow> details = new ArrayList<>();
		for(Customer customer : customerBusiness.findAll())
			details.add(new CustomerReportTableRow(customer));
		
		table = createDetailsTable(CustomerReportTableRow.class, new DetailsConfigurationListener.Table.Adapter<Customer, CustomerReportTableRow>(Customer.class, CustomerReportTableRow.class){
			private static final long serialVersionUID = 1L;
			@Override
			public ColumnAdapter getColumnAdapter() {
				return new ColumnAdapter(){
					@Override
					public Boolean isColumn(Field field) {
						return CustomerReportTableRow.saleStockFieldIgnored(field);
					}
				};
			}
		});	
		/*
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportSaleStock());
				*/
	}
	
}