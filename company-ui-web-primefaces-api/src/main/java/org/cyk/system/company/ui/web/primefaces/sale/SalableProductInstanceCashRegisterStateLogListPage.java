package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterStateLogDetails;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateLog;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.web.api.WebManager;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterStateLogListPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateLog,SalableProductInstanceCashRegisterStateLogDetails> TABLE_ADAPTER = new TableAdapter();
	
	private Table<SalableProductInstanceCashRegisterStateLogDetails> table;
	private DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateLog,SalableProductInstanceCashRegisterStateLogDetails> tableAdapter = TABLE_ADAPTER;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table = createDetailsTable(SalableProductInstanceCashRegisterStateLogDetails.class, tableAdapter);	
		table.setRendered(Boolean.TRUE);
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(FiniteStateMachineStateLog.class);
		/*table.getPrintCommandable().setRendered(Boolean.TRUE);
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), 
				CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
		table.getPrintCommandable().addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), balanceType);*/
	}
		
	public static class TableAdapter extends DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateLog,SalableProductInstanceCashRegisterStateLogDetails> {

		private static final long serialVersionUID = 202093846160625878L;

		//private Collection<FiniteStateMachineStateLog> finiteStateMachineStateLogs;
		
		public TableAdapter() {
			super(FiniteStateMachineStateLog.class, SalableProductInstanceCashRegisterStateLogDetails.class);
		}
		
		@Override
		public Boolean getEnabledInDefaultTab() {
			return Boolean.TRUE;
		}
		
		protected FiniteStateMachineStateLog.SearchCriteria getSearchCriteria(){
			FiniteStateMachineStateLog.SearchCriteria searchCriteria = new FiniteStateMachineStateLog.SearchCriteria();
			searchCriteria.addFiniteStateMachineStates(WebManager.getInstance().decodeIdentifiablesRequestParameter(FiniteStateMachineState.class));
			if(searchCriteria.getFiniteStateMachineStates().isEmpty())
				searchCriteria.addFiniteStateMachineStates(RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness()
						.findByMachine(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration()
								.getSalableProductInstanceCashRegisterFiniteStateMachine()));
			return searchCriteria;
		}
		
		@Override
		public Collection<FiniteStateMachineStateLog> getIdentifiables() {
			return RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().findByCriteria(getSearchCriteria());
		}
		
		@Override
		public Collection<SalableProductInstanceCashRegisterStateLogDetails> getDatas() {
			Collection<SalableProductInstanceCashRegisterStateLogDetails> collection = super.getDatas();
			FiniteStateMachineStateLog.IdentifiablesSearchCriteria<SalableProductInstanceCashRegister> searchCriteria = new FiniteStateMachineStateLog
					.IdentifiablesSearchCriteria<>(SalableProductInstanceCashRegister.class,getSearchCriteria());
			Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness()
					.findIdentifiablesByCriteria(searchCriteria);
			return CompanyReportRepository.getInstance().format(collection,salableProductInstanceCashRegisters);
		}
		
		public ColumnAdapter getColumnAdapter() {
			return new ColumnAdapter(){
				@Override
				public Boolean isColumn(Field field) {
					if( SalableProductInstanceCashRegisterStateLogDetails.FIELD_STATE.equals(field.getName()) ){
						Collection<Long> identifiers = WebManager.getInstance().decodeIdentifiersRequestParameter(UIManager.getInstance().businessEntityInfos(FiniteStateMachineState.class)
								.getIdentifier());
						return identifiers==null || identifiers.size() > 1;
					}
					return !ArrayUtils.contains(new String[]{SalableProductInstanceCashRegisterStateLogDetails.FIELD_PARTY
							,SalableProductInstanceCashRegisterStateLogDetails.FIELD_IDENTIFIABLE_GLOBAL_IDENTIFIER}, field.getName());
				}
			};
		}
		
	}
	
}