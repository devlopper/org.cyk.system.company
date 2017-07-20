package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterStateLogDetails;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifier;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.web.api.WebManager;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterStateLogListPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateIdentifiableGlobalIdentifier,SalableProductInstanceCashRegisterStateLogDetails> TABLE_ADAPTER = new TableAdapter();
	
	private Table<SalableProductInstanceCashRegisterStateLogDetails> table;
	private DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateIdentifiableGlobalIdentifier,SalableProductInstanceCashRegisterStateLogDetails> tableAdapter = TABLE_ADAPTER;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table = createDetailsTable(SalableProductInstanceCashRegisterStateLogDetails.class, tableAdapter);	
		table.setRendered(Boolean.TRUE);
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(FiniteStateMachineStateIdentifiableGlobalIdentifier.class);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		//FiniteStateMachineStateIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = ((TableAdapter)tableAdapter).getSearchCriteria();
		//contentTitle = languageBusiness.findListOfText(SalableProductInstance.class)+Constant.CHARACTER_SPACE+StringUtils.join(searchCriteria.getFiniteStateMachineStates(),Constant.CHARACTER_COMA);
	}
	
	@Getter @Setter
	public static class TableAdapter extends DetailsConfigurationListener.Table.Adapter<FiniteStateMachineStateIdentifiableGlobalIdentifier,SalableProductInstanceCashRegisterStateLogDetails> {

		private static final long serialVersionUID = 202093846160625878L;

		private String timeDivisionTypeCode = RootConstant.Code.TimeDivisionType.DAY;
		
		public TableAdapter() {
			super(FiniteStateMachineStateIdentifiableGlobalIdentifier.class, SalableProductInstanceCashRegisterStateLogDetails.class);
		}
		
		@Override
		public Boolean getEnabledInDefaultTab() {
			return Boolean.TRUE;
		}
		
		public FiniteStateMachineStateIdentifiableGlobalIdentifier.SearchCriteria getSearchCriteria(){
			FiniteStateMachineStateIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = new FiniteStateMachineStateIdentifiableGlobalIdentifier.SearchCriteria();
			/*searchCriteria.addFiniteStateMachineStates(WebManager.getInstance().decodeIdentifiablesRequestParameter(FiniteStateMachineState.class));
			searchCriteria.setTimeDivisionTypeCode(timeDivisionTypeCode);
			if(searchCriteria.getFiniteStateMachineStates().isEmpty())
				searchCriteria.addFiniteStateMachineStates(inject(FiniteStateMachineStateBusiness.class)
						.findByMachine(inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration()
								.getSalableProductInstanceCashRegisterFiniteStateMachine()));
			*/
			return searchCriteria;
		}
		
		@Override
		public Collection<FiniteStateMachineStateIdentifiableGlobalIdentifier> getIdentifiables() {
			return inject(FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness.class).findByCriteria(getSearchCriteria());
		}
		
		@Override
		public Collection<SalableProductInstanceCashRegisterStateLogDetails> getDatas() {
			Collection<SalableProductInstanceCashRegisterStateLogDetails> collection = super.getDatas();
			FiniteStateMachineStateIdentifiableGlobalIdentifier.IdentifiablesSearchCriteria<SalableProductInstanceCashRegister> searchCriteria = new FiniteStateMachineStateIdentifiableGlobalIdentifier
					.IdentifiablesSearchCriteria<>(SalableProductInstanceCashRegister.class,getSearchCriteria());
			Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = inject(FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness.class)
					.findIdentifiablesByCriteria(searchCriteria);
			return CompanyReportRepository.getInstance().format(collection,salableProductInstanceCashRegisters,getTimeDivisionTypeCode());
		}
		
		public ColumnAdapter getColumnAdapter() {
			return new ColumnAdapter(){
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean isColumn(Field field) {
					if( SalableProductInstanceCashRegisterStateLogDetails.FIELD_FINITE_STATE_MACHINE_STATE.equals(field.getName()) ){
						Collection<Long> identifiers = WebManager.getInstance().decodeIdentifiersRequestParameter(UIManager.getInstance().businessEntityInfos(FiniteStateMachineState.class)
								.getIdentifier());
						return identifiers==null || identifiers.size() > 1;
					}
					return Boolean.TRUE;
				}
			};
		}
		
	}
	
}