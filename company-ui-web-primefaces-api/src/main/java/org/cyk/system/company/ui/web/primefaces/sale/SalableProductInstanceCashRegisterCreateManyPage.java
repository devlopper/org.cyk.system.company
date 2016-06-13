package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineFinalState;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterCreateManyPage extends AbstractCrudOnePage<SalableProductInstanceCashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private CashRegister cashRegister;
	private FiniteStateMachineFinalState finiteStateMachineFinalState;
	private List<CashRegister> cashRegisters;
	private List<SalableProductInstance> salableProductInstances;
	private ItemCollection<SalableProductInstanceCashRegisterItem,SalableProductInstanceCashRegister> salableProductInstanceCashRegisterCollection;
	
	@Override
	protected void initialisation() {
		Long cashRegisterIdentifier = requestParameterLong(CashRegister.class);
		if(cashRegisterIdentifier==null){
			cashRegisters = new ArrayList<>(CompanyBusinessLayer.getInstance().getCashRegisterBusiness().findAll());
		}else{
			cashRegister = CompanyBusinessLayer.getInstance().getCashRegisterBusiness().find(cashRegisterIdentifier);
		}
			
		super.initialisation();
		
		if(cashRegister!=null){
			
		}
		
		if(Crud.CREATE.equals(crud)){
			
		}else{
			
		}
		/*
		salableProductInstanceCashRegisterCollection = createItemCollection(SalableProductInstanceCashRegisterItem.class, SalableProductInstanceCashRegister.class,new ItemCollectionWebAdapter<SalableProductInstanceCashRegisterItem,SalableProductInstanceCashRegister>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public Collection<SalableProductInstanceCashRegister> create() {
				return identifiable.getSalableProductInstanceCashRegisters();
			}
			@Override
			public Collection<SalableProductInstanceCashRegister> load() {
				return identifiable.getSalableProductInstanceCashRegisters();
			}
			@Override
			public Crud getCrud() {
				return crud;
			}
			@Override
			public Boolean isShowAddButton() {
				return Boolean.FALSE;
			}
			@Override
			public void instanciated(AbstractItemCollection<SalableProductInstanceCashRegisterItem, SalableProductInstanceCashRegister,SelectItem> itemCollection,SalableProductInstanceCashRegisterItem mark) {
				super.instanciated(itemCollection, mark);
				mark.setRegistrationCode(mark.getIdentifiable().getStudentSubject().getStudent().getRegistration().getCode());
				mark.setNames(mark.getIdentifiable().getStudentSubject().getStudent().getPerson().getNames());
				mark.setValue(mark.getIdentifiable().getValue());
				mark.setValueAsString(RootBusinessLayer.getInstance().getNumberBusiness().format(mark.getValue()));
			}	
			@Override
			public void write(SalableProductInstanceCashRegisterItem item) {
				super.write(item);
				item.getIdentifiable().setValue(item.getValue());
			}
		});
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
	/*
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		setChoices(Form.FIELD_TYPE, SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectEvaluationTypeBusiness().findByClassroomSessionDivisionSubject(classroomSessionDivisionSubject));
	}
	
	@Override
	protected void create() {
		identifiable.setSalableProductInstanceCashRegisters(markCollection.getIdentifiables());
		super.create();
		schoolWebManager.initialiseNavigatorTree(userSession);
	}
	
	@Override
	protected void update() {
		SchoolBusinessLayer.getInstance().getEvaluationBusiness().save(identifiable,markCollection.getIdentifiables());
	}
	
	@Override
	protected void delete() {
		super.delete();
		schoolWebManager.initialiseNavigatorTree(userSession);
	}
	
	@Override
	protected Boolean consultOnSuccess() {
		return Boolean.TRUE;
	}
	
	protected Evaluation instanciateIdentifiable() {
		Evaluation subjectEvaluation = SchoolBusinessLayer.getInstance().getEvaluationBusiness().newInstance(classroomSessionDivisionSubject);
		subjectEvaluation.setClassroomSessionDivisionSubjectEvaluationType(subjectEvaluationType);
		return subjectEvaluation;
	}
	*/	
	@Override
	protected Class<?> __formModelClass__() {
		return Form.class;
	}
		
	@Getter @Setter
	public static class Form extends AbstractFormModel<SalableProductInstanceCashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
	
	@Getter @Setter
	public static class SalableProductInstanceCashRegisterItem extends AbstractItemCollectionItem<SalableProductInstanceCashRegister> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private SalableProductInstance salableProductInstance;
		private CashRegister cashRegister;
		private FiniteStateMachineFinalState finiteStateMachineFinalState;
		
		/*
		@Override
		public String toString() {
			return salableProductInstanceCode+Constant.CHARACTER_SPACE+cashRegister;
		}*/
	}
	
	/**/
	
}
