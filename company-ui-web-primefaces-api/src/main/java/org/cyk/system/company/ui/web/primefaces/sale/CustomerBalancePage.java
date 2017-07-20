package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.CustomerReportTableRow;
import org.cyk.system.company.model.sale.Customer;
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
		Collection<CustomerReportTableRow> details = new ArrayList<>();
		String balanceType = requestParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType());
		final Boolean all = CompanyReportRepository.getInstance().getParameterCustomerBalanceAll().equals(balanceType);
		contentTitle = all?text("company.command.customer.balance"):text("field.credence");
		
		Collection<Customer> customers = all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
		for(Customer customer : customers)
			details.add(new CustomerReportTableRow(customer));
		
		table = createDetailsTable(CustomerReportTableRow.class, new DetailsConfigurationListener.Table.Adapter<Customer,CustomerReportTableRow>(Customer.class, CustomerReportTableRow.class){
			private static final long serialVersionUID = -6570916902889942385L;
			
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
			
			@Override
			public Collection<Customer> getIdentifiables() {
				return all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
			}
			
			@Override
			public ColumnAdapter getColumnAdapter() {
				return new ColumnAdapter(){
					private static final long serialVersionUID = 1L;

					@Override
					public Boolean isColumn(Field field) {
						return all?CustomerReportTableRow.balanceFieldIgnored(field):CustomerReportTableRow.credenceFieldIgnored(field);
					}
				};
			}
		});	
		table.setRendered(Boolean.TRUE);
		table.setShowHeader(Boolean.TRUE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().setRendered(Boolean.TRUE);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), balanceType);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		/*Collection<CustomerReportTableRow> details = new ArrayList<>();
		String balanceType = requestParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType());
		final Boolean all = CompanyReportRepository.getInstance().getParameterCustomerBalanceAll().equals(balanceType);
		contentTitle = all?text("company.command.customer.balance"):text("field.credence");
		
		Collection<Customer> customers = all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
		for(Customer customer : customers)
			details.add(new CustomerReportTableRow(customer));
		
		table = createDetailsTable(CustomerReportTableRow.class, new DetailsConfigurationListener.Table.Adapter<Customer,CustomerReportTableRow>(Customer.class, CustomerReportTableRow.class){
			private static final long serialVersionUID = -6570916902889942385L;
			@Override
			public Collection<Customer> getIdentifiables() {
				return customerBusiness.findAll();// all?customerBusiness.findAll():customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
			}
			@Override
			public ColumnAdapter getColumnAdapter() {
				return new ColumnAdapter(){
					@Override
					public Boolean isColumn(Field field) {
						System.out.println(
								"CustomerBalancePage.afterInitialisation().new Adapter() {...}.getColumnAdapter().new ColumnAdapter() {...}.isColumn() : "+field.getName());
						return true;
						//return all?CustomerReportTableRow.balanceFieldIgnored(field):CustomerReportTableRow.credenceFieldIgnored(field);
					}
				};
			}
		});	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), balanceType);*/
		
		
		debug(table);
	}
	
}