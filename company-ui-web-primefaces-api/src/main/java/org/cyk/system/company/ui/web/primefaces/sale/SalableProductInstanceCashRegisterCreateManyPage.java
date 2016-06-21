package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterCreateManyPage extends AbstractCrudOnePage<SalableProductInstanceCashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private SalableProductInstance salableProductInstance;
	private List<SalableProductInstance> salableProductInstances;
	
	private CashRegister cashRegister;
	private List<CashRegister> cashRegisters;
	
	private FiniteStateMachineState finiteStateMachineState;
	private List<FiniteStateMachineState> finiteStateMachineStates;
	
	private ItemCollection<SalableProductInstanceCashRegisterItem,SalableProductInstanceCashRegister> itemCollection;
	
	@Override
	protected void initialisation() {
		salableProductInstances = new ArrayList<>(CompanyBusinessLayer.getInstance().getSalableProductInstanceBusiness().findWhereNotAssociatedToCashRegister());
		
		Long cashRegisterIdentifier = requestParameterLong(CashRegister.class);
		if(cashRegisterIdentifier==null){
			
		}else{
			cashRegister = CompanyBusinessLayer.getInstance().getCashRegisterBusiness().find(cashRegisterIdentifier);
		}
		cashRegisters = new ArrayList<>(CompanyBusinessLayer.getInstance().getCashRegisterBusiness().findAll());
		
		Long finiteStateMachineStateIdentifier = requestParameterLong(FiniteStateMachineState.class);
		if(finiteStateMachineStateIdentifier==null){
			
		}else{
			finiteStateMachineState = RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness().find(finiteStateMachineStateIdentifier);
		}
		finiteStateMachineStates = new ArrayList<>(RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness()
				.findByMachine(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration()
						.getSalableProductInstanceCashRegisterFiniteStateMachine()));
		
		super.initialisation();
		
		if(cashRegister!=null){
			
		}
		
		if(Crud.CREATE.equals(crud)){
			
		}else{
			
		}
		
		itemCollection = createItemCollection(SalableProductInstanceCashRegisterItem.class, SalableProductInstanceCashRegister.class,new ItemCollection.Adapter<SalableProductInstanceCashRegisterItem,SalableProductInstanceCashRegister>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public Crud getCrud() {
				return crud;
			}
			@Override
			public Boolean isShowAddButton() {
				return Boolean.TRUE;
			}
			
			@Override
			public void instanciated(AbstractItemCollection<SalableProductInstanceCashRegisterItem, SalableProductInstanceCashRegister, SelectItem> itemCollection,
					SalableProductInstanceCashRegisterItem item) {
				super.instanciated(itemCollection, item);
				item.setCashRegister(((Form)form.getData()).cashRegister);
				item.setSalableProductInstance(((Form)form.getData()).salableProductInstance);
				item.setFiniteStateMachineState(((Form)form.getData()).finiteStateMachineState);
			}
			
			@Override
			public void write(SalableProductInstanceCashRegisterItem item) {
				super.write(item);
				item.getIdentifiable().setCashRegister(item.getCashRegister());
				item.getIdentifiable().setSalableProductInstance(item.getSalableProductInstance());
				item.getIdentifiable().setFiniteStateMachineState(item.getFiniteStateMachineState());
			}
			
		});
		/*
		((AbstractWebApplicableValueQuestion)markCollection.getApplicableValueQuestion()).setUpdate("markValue");
		markCollection.getDeleteCommandable().setRendered(Boolean.FALSE);
		markCollection.getApplicableValueQuestion().setRendered(Boolean.TRUE);
		//markCollection.getAddCommandable().setRendered(Boolean.FALSE);
		form.getControlSetListeners().add(new ControlSetAdapter<Object>(){
			@Override
			public Boolean build(Field field) {
				if(field.getName().equals(Form.FIELD_TYPE))
					return subjectEvaluationType == null;
				return false;
			}
		});
		
		//TODO make it in super class
		//markCollection.setShowFooter(markCollection.getAddCommandable().getRendered());
		//onDocumentLoadJavaScript = markCollection.getFormatJavaScript();
		*/
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		setChoices(Form.FIELD_SALABLEPRODUCTINSTANCE, salableProductInstances,salableProductInstance);
		setChoices(Form.FIELD_CASHREGISTER, cashRegisters,cashRegister);
		setChoices(Form.FIELD_FINITESTATEMACHINESTATE, finiteStateMachineStates,finiteStateMachineState);
	}
	
	@Override
	protected void create() {
		CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterBusiness().create(itemCollection.getIdentifiables(),userSession.getUser());
	}
	
	@Override
	protected Class<?> __formModelClass__() {
		return Form.class;
	}
		
	@Getter @Setter
	public static class Form extends AbstractFormModel<SalableProductInstanceCashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private CashRegister cashRegister;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private SalableProductInstance salableProductInstance;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private FiniteStateMachineState finiteStateMachineState;
		
		public static final String FIELD_CASHREGISTER = "cashRegister";
		public static final String FIELD_SALABLEPRODUCTINSTANCE = "salableProductInstance";
		public static final String FIELD_FINITESTATEMACHINESTATE = "finiteStateMachineState";
	}
	
	@Getter @Setter
	public static class SalableProductInstanceCashRegisterItem extends AbstractItemCollectionItem<SalableProductInstanceCashRegister> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private SalableProductInstance salableProductInstance;
		private CashRegister cashRegister;
		private FiniteStateMachineState finiteStateMachineState;
		
	}
	
	/**/
	
}