package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineStateBusiness;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.api.model.AbstractQueryManyFormModel;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.api.WebManager;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.AbstractProcessManyPage;
import org.cyk.ui.web.primefaces.page.AbstractSelectManyPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;
import org.cyk.utility.common.cdi.AbstractBean;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Getter @Setter
public class SalableProductInstanceCashRegisterQueryManyFormModel extends AbstractQueryManyFormModel.Default<SalableProductInstanceCashRegister> implements Serializable {
	private static final long serialVersionUID = -3756660150800681378L;
	
	@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo @NotNull
	//@Sequence(direction=Direction.BEFORE,field=FIELD_FINITESTATEMACHINESTATE)
	private CashRegister cashRegister;
	//@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private SalableProductInstance salableProductInstance;
	
	@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo @NotNull 
	//@Sequence(direction=Direction.BEFORE,field=FIELD_IDENTIFIABLES)
	private FiniteStateMachineState finiteStateMachineState;
	/*
	public SalableProductInstanceCashRegisterQueryManyFormModel() {
		showIdentifiables = Boolean.FALSE;
		showCodes = Boolean.TRUE;
	}*/
	
	@Override @Sequence(direction=Direction.AFTER,field=FIELD_FINITESTATEMACHINESTATE)
	public List<SalableProductInstanceCashRegister> getIdentifiables() {
		return super.getIdentifiables();
	}
	
	/**/
	
	public static final String FIELD_CASHREGISTER = "cashRegister";
	//public static final String FIELD_SALABLEPRODUCTINSTANCE = "salableProductInstance";
	public static final String FIELD_FINITESTATEMACHINESTATE = "finiteStateMachineState";
	
	/**/
	
	@Getter @Setter
	public static class PageAdapter extends AbstractSelectManyPage.Listener.Adapter.Default<SalableProductInstanceCashRegister,Long> implements Serializable {

		private static final long serialVersionUID = -7392513843271510254L;
		
		public PageAdapter() {
			super(SalableProductInstanceCashRegister.class);
		}
		
		@Override
		public void initialisationEnded(AbstractBean bean) {
			super.initialisationEnded(bean);
			((AbstractSelectManyPage<?>)bean).getForm().getControlSetListeners().add(new ControlSetAdapter<Object>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Boolean build(Field field) {
					if(field.getName().equals(FIELD_FINITESTATEMACHINESTATE)){
						FiniteStateMachineAlphabet finiteStateMachineAlphabet = getFiniteStateMachineAlphabet(); 
						
						return finiteStateMachineAlphabet==null;
					}
					return super.build(field);
				}
				
				@Override
				public String fiedLabel(ControlSet<Object, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,Field field) {
					if(field.getName().equals(FIELD_IDENTIFIABLES))
						return inject(LanguageBusiness.class).findClassLabelText(SalableProduct.class);
					return super.fiedLabel(controlSet, field);
				}
			});
		}
		
		@Override
		protected void initialiseSelect(AbstractSelectManyPage<?> page) {
			super.initialiseSelect(page);
			selectCashRegister(page);
		}
		
		private FiniteStateMachineAlphabet getFiniteStateMachineAlphabet(){
			return WebManager.getInstance().getIdentifiableFromRequestParameter(FiniteStateMachineAlphabet.class,Boolean.TRUE);
		}
		
		private Collection<FiniteStateMachineState> getFiniteStateMachineStates(){
			FiniteStateMachine finiteStateMachine = inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration()
					.getSalableProductInstanceCashRegisterFiniteStateMachine();
			FiniteStateMachineAlphabet finiteStateMachineAlphabet = getFiniteStateMachineAlphabet();
			return finiteStateMachineAlphabet == null ? inject(FiniteStateMachineStateBusiness.class)
					.findByMachine(finiteStateMachine) : inject(FiniteStateMachineStateBusiness.class)
					.findFromByMachineByAlphabet(finiteStateMachine, finiteStateMachineAlphabet);
		}
				
		
		private void selectCashRegister(final AbstractSelectManyPage<?> page){
			Collection<CashRegister> cashRegisters = Boolean.TRUE.equals(page.getUserSession().getIsAdministrator()) 
					?inject(CashRegisterBusiness.class).findAll() : inject(CashRegisterBusiness.class)
							.findByPerson((Person)page.getUserSession().getUser());
			CashRegister cashRegister = (CashRegister) page.setChoicesAndGetAutoSelected(FIELD_CASHREGISTER, cashRegisters);
			page.createAjaxBuilder(FIELD_CASHREGISTER).crossedFieldNames(FIELD_FINITESTATEMACHINESTATE).updatedFieldNames(FIELD_IDENTIFIABLES)
			.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
				@Override
				public void execute(CashRegister cashRegister) {			
					selectSalableProductInstanceCashRegisters(page,cashRegister,page.getForm().findInputByFieldName(FIELD_FINITESTATEMACHINESTATE)==null 
							? ((SalableProductInstanceCashRegisterQueryManyFormModel)page.getForm().getData()).finiteStateMachineState : (FiniteStateMachineState) page.getForm().findInputByFieldName(FIELD_FINITESTATEMACHINESTATE).getValue());
				}
			}).build();
			
			//page.setChoices(FIELD_SALABLEPRODUCTINSTANCE, inject(SalableProductInstanceBusiness.class).findAll());
			
			Collection<FiniteStateMachineState> finiteStateMachineStates = getFiniteStateMachineStates();
			
			FiniteStateMachineState finiteStateMachineState = (FiniteStateMachineState) page.setChoicesAndGetAutoSelected(FIELD_FINITESTATEMACHINESTATE,finiteStateMachineStates );
			((SalableProductInstanceCashRegisterQueryManyFormModel)page.getForm().getData()).finiteStateMachineState = finiteStateMachineState;
			page.createAjaxBuilder(FIELD_FINITESTATEMACHINESTATE).crossedFieldNames(FIELD_CASHREGISTER).updatedFieldNames(FIELD_IDENTIFIABLES)
			.method(FiniteStateMachineState.class,new ListenValueMethod<FiniteStateMachineState>() {
				@Override
				public void execute(FiniteStateMachineState finiteStateMachineState) {
					selectSalableProductInstanceCashRegisters(page,(CashRegister)page.getForm().findInputByFieldName(FIELD_CASHREGISTER).getValue(),finiteStateMachineState);
				}
			}).build();
			
			selectSalableProductInstanceCashRegisters(page, cashRegister, finiteStateMachineState);
		}
	
		private void selectSalableProductInstanceCashRegisters(AbstractSelectManyPage<?> page,CashRegister cashRegister,FiniteStateMachineState finiteStateMachineState){
			Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters;
			if(cashRegister==null || finiteStateMachineState==null){
				salableProductInstanceCashRegisters = new ArrayList<>();
			}else{
				SearchCriteria searchCriteria = new SearchCriteria();
				searchCriteria.addCashRegisters(Arrays.asList(cashRegister)).addFiniteStateMachineStates(Arrays.asList(finiteStateMachineState));
				salableProductInstanceCashRegisters = inject(SalableProductInstanceCashRegisterBusiness.class).findByCriteria(searchCriteria);
			}
			
			page.setChoices(FIELD_IDENTIFIABLES, salableProductInstanceCashRegisters);
		}
		
		@Override
		public Object[] getParameters(AbstractSelectManyPage<?> page,Object data, String actionIdentifier) {
			if(CompanyBusinessLayer.getInstance().getActionUpdateSalableProductInstanceCashRegisterState().equals(actionIdentifier)){
				FiniteStateMachineState finiteStateMachineState = ((SalableProductInstanceCashRegisterQueryManyFormModel)data).finiteStateMachineState;
				if(finiteStateMachineState==null)
					;
				else{
					finiteStateMachineState = inject(FiniteStateMachineStateBusiness.class).findByFromStateByAlphabet(finiteStateMachineState, 
							getFiniteStateMachineAlphabet());
					return new Object[]{UIManager.getInstance().businessEntityInfos(FiniteStateMachineState.class).getIdentifier(),finiteStateMachineState.getIdentifier()};
				}
			}
			return super.getParameters(page, data, actionIdentifier);
		}
	}
	
	
	
	/**/
	
	@Getter @Setter
	public static class ProcessPageAdapter extends AbstractProcessManyPage.Listener.Adapter.Default<SalableProductInstanceCashRegister,Long> implements Serializable {

		private static final long serialVersionUID = -7392513843271510254L;
		
		public ProcessPageAdapter() {
			super(SalableProductInstanceCashRegister.class);
		}
		
		@Override
		protected void initialiseProcessOnInitialisationEnded(final AbstractProcessManyPage<?> page) {
			super.initialiseProcessOnInitialisationEnded(page);
			CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
			page.getForm().getSubmitCommandable().getCommand().setConfirm(Boolean.TRUE);
			page.getForm().getControlSetListeners().add(new ControlSetAdapter<Object>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean build(Field field) {
					if(field.getName().equals(Form.FIELD_FINITESTATEMACHINESTATE))
						return getFiniteStateMachineState() == null;
					return super.build(field);
				}
			});
			if(companyBusinessLayer.getActionUpdateSalableProductInstanceCashRegisterState().equals(page.getActionIdentifier())){
				
			}
		}
		
		private FiniteStateMachineState getFiniteStateMachineState(){
			return WebManager.getInstance().getIdentifiableFromRequestParameter(FiniteStateMachineState.class,Boolean.TRUE);
		}
		
		@Override
		protected void initialiseProcessOnAfterInitialisationEnded(AbstractProcessManyPage<?> page) {
			super.initialiseProcessOnAfterInitialisationEnded(page);
			page.setChoices(Form.FIELD_FINITESTATEMACHINESTATE, inject(FiniteStateMachineStateBusiness.class)
					.findByMachine(inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration()
							.getSalableProductInstanceCashRegisterFiniteStateMachine())
							,((Form)page.getForm().getData()).finiteStateMachineState = getFiniteStateMachineState());
		}
		
		@Override
		public void serve(AbstractProcessManyPage<?> page,Object data, String actionIdentifier) {
			CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
			((Form)data).getFiniteStateMachineState().getProcessing().setParty(page.getUserSession().getUser());
			((Form)data).getFiniteStateMachineState().getProcessing().setDate(((Form)data).date);
			if(companyBusinessLayer.getActionUpdateSalableProductInstanceCashRegisterState().equals(actionIdentifier)){
				Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = new ArrayList<>();
				for(Object object : page.getElements()){
					((SalableProductInstanceCashRegister) object).setFiniteStateMachineState(((Form)data).getFiniteStateMachineState());
					salableProductInstanceCashRegisters.add((SalableProductInstanceCashRegister) object);
				}
				inject(SalableProductInstanceCashRegisterBusiness.class).update(salableProductInstanceCashRegisters);
			}
		}
		
		@Override
		public Class<?> getFormDataClass(AbstractProcessManyPage<?> processManyPage,String actionIdentifier) {
			return Form.class;
		}
		
		@Override
		public Boolean getShowForm(AbstractProcessManyPage<?> processManyPage,String actionIdentifier) {
			return ArrayUtils.contains(new String[]{CompanyBusinessLayer.getInstance().getActionUpdateSalableProductInstanceCashRegisterState()}, actionIdentifier);
		}
		
		@Getter @Setter
		public static class Form extends AbstractFormModel<SalableProductInstanceCashRegister> implements Serializable{
			private static final long serialVersionUID = -4741435164709063863L;
			
			@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private FiniteStateMachineState finiteStateMachineState;
			@Input @InputCalendar(format=Format.DATETIME_SHORT) private Date date = new Date();
			
			public static final String FIELD_FINITESTATEMACHINESTATE = "finiteStateMachineState";
		}
		
	}
	
}