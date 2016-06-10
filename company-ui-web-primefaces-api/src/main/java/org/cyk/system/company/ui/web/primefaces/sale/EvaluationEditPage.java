package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.Evaluation;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubjectEvaluation;
import org.cyk.system.school.ui.web.primefaces.SchoolWebManager;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.AbstractWebApplicableValueQuestion;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class EvaluationEditPage extends AbstractCrudOnePage<SalableProductInstanceCashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private CashRegister cashRegister;
	private ItemCollection<SalableProductInstanceCashRegisterItem,SalableProductInstanceCashRegister> markCollection;
	
	@Override
	protected void initialisation() {
		Long subjectEvaluationTypeIdentifier = requestParameterLong(ClassroomSessionDivisionSubjectEvaluationType.class);
		if(subjectEvaluationTypeIdentifier==null){
			Long classroomSessionDivisionSubjectIdentifier = requestParameterLong(ClassroomSessionDivisionSubject.class);
			if(classroomSessionDivisionSubjectIdentifier==null)
				;
			else
				classroomSessionDivisionSubject = SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectBusiness().find(classroomSessionDivisionSubjectIdentifier);	
		}else{
			subjectEvaluationType = SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectEvaluationTypeBusiness().find(subjectEvaluationTypeIdentifier);
			classroomSessionDivisionSubject = subjectEvaluationType.getClassroomSessionDivisionSubject();
		}
			
		super.initialisation();
		if(subjectEvaluationType!=null){
			maximumValue = identifiable.getClassroomSessionDivisionSubjectEvaluationType().getMaximumValue();
		}
		if(Crud.CREATE.equals(crud)){
			
		}else{
			identifiable.setSalableProductInstanceCashRegisters(SchoolBusinessLayer.getInstance().getStudentClassroomSessionDivisionSubjectEvaluationBusiness().findByEvaluation(identifiable,Crud.UPDATE.equals(crud)));
			subjectEvaluationType = identifiable.getClassroomSessionDivisionSubjectEvaluationType();
			classroomSessionDivisionSubject = subjectEvaluationType.getClassroomSessionDivisionSubject();
		}
		
		markCollection = createItemCollection(SalableProductInstanceCashRegisterItem.class, StudentClassroomSessionDivisionSubjectEvaluation.class,new ItemCollectionWebAdapter<SalableProductInstanceCashRegisterItem,StudentClassroomSessionDivisionSubjectEvaluation>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public Collection<StudentClassroomSessionDivisionSubjectEvaluation> create() {
				return identifiable.getSalableProductInstanceCashRegisters();
			}
			@Override
			public Collection<StudentClassroomSessionDivisionSubjectEvaluation> load() {
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
			public void instanciated(AbstractItemCollection<SalableProductInstanceCashRegisterItem, StudentClassroomSessionDivisionSubjectEvaluation,SelectItem> itemCollection,SalableProductInstanceCashRegisterItem mark) {
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
		
	}
	
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
		
	@Override
	protected Class<?> __formModelClass__() {
		return Form.class;
	}
		
	@Getter @Setter
	public static class Form extends AbstractFormModel<Evaluation> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
	
	@Getter @Setter
	public static class SalableProductInstanceCashRegisterItem extends AbstractItemCollectionItem<SalableProductInstanceCashRegister> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private String registrationCode;
		private String names;
		private BigDecimal value;
		private String valueAsString;
				
		@Override
		public String toString() {
			return registrationCode+" "+names+" "+value;
		}
	}
	
	/**/
	
}
