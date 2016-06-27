package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterStateLogDetails;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateLog;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterStateLogListPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private Table<SalableProductInstanceCashRegisterStateLogDetails> table;
	private Collection<FiniteStateMachineStateLog> finiteStateMachineStateLogs;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table = createDetailsTable(SalableProductInstanceCashRegisterStateLogDetails.class, new DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateLog,SalableProductInstanceCashRegisterStateLogDetails>(FiniteStateMachineStateLog.class, SalableProductInstanceCashRegisterStateLogDetails.class){
			private static final long serialVersionUID = -6570916902889942385L;
			
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
			
			@Override
			public Collection<FiniteStateMachineStateLog> getIdentifiables() {
				return finiteStateMachineStateLogs = RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().findAll();
			}
			
			@Override
			public Collection<SalableProductInstanceCashRegisterStateLogDetails> getDatas() {
				Collection<SalableProductInstanceCashRegisterStateLogDetails> collection = super.getDatas();
				Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness()
						.findByClass(finiteStateMachineStateLogs, SalableProductInstanceCashRegister.class);
				for(SalableProductInstanceCashRegisterStateLogDetails salableProductInstanceCashRegisterStateLogDetails : collection){
					for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters){
						if(salableProductInstanceCashRegisterStateLogDetails.getMaster().getIdentifiableGlobalIdentifier().equals(salableProductInstanceCashRegister.getGlobalIdentifier())){
							salableProductInstanceCashRegisterStateLogDetails.setInstanceCode(salableProductInstanceCashRegister.getSalableProductInstance().getCode());
							break;
						}
					}
				}
				return collection;
			}
			
			/*@Override
			public ColumnAdapter getColumnAdapter() {
				return new ColumnAdapter(){
					@Override
					public Boolean isColumn(Field field) {
						return all?CustomerReportTableRow.balanceFieldIgnored(field):CustomerReportTableRow.credenceFieldIgnored(field);
					}
				};
			}*/
		});	
		table.setRendered(Boolean.TRUE);
		table.setShowHeader(Boolean.TRUE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(FiniteStateMachineStateLog.class);
		/*table.getPrintCommandable().setRendered(Boolean.TRUE);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), balanceType);*/
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
		
	}
	
}