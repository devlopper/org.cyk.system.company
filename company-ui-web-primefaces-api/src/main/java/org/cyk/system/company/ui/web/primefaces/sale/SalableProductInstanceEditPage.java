package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.CodesFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceEditPage extends AbstractCrudOnePage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean CREATE_ON_SALABLE_PRODUCT = Boolean.TRUE;
	
	public static ControlSetAdapter<Object> CREATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean build(Object data,Field field) {
			if(Boolean.TRUE.equals(CREATE_ON_SALABLE_PRODUCT))
				return ArrayUtils.contains(new String[]{SalableProductInstanceEditPage.Form.FIELD_SALABLE_PRODUCT,CodesFormModel.FIELD_CODE}, field.getName());
			else
				return !ArrayUtils.contains(new String[]{CodesFormModel.FIELD_CODE}, field.getName());
		}
		
		public void input(org.cyk.ui.api.data.collector.form.ControlSet<Object,DynaFormModel,DynaFormRow,DynaFormLabel,DynaFormControl,SelectItem> controlSet, org.cyk.ui.api.data.collector.control.Input<?,DynaFormModel,DynaFormRow,DynaFormLabel,DynaFormControl,SelectItem> input) {
			
		}
	};
	
	public static ControlSetAdapter<Object> UPDATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean build(Object data,Field field) {
			return ArrayUtils.contains(new String[]{SalableProductInstanceEditPage.Form.FIELD_SALABLE_PRODUCT,CodesFormModel.FIELD_CODE}, field.getName());
		}
	};
	
	@Override
	protected void initialisation() {
		super.initialisation();
		if(Crud.CREATE.equals(crud)){
			if(CREATE_ON_SALABLE_PRODUCT!=null){
				form.getControlSetListeners().add(CREATE_CONTROL_SET_ADAPTER);
				
			}
		}else if(Crud.UPDATE.equals(crud)){
			form.getControlSetListeners().add(UPDATE_CONTROL_SET_ADAPTER);
		}
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		if(Crud.CREATE.equals(crud)){
			/*
			if(CREATE_ON_SALABLE_PRODUCT!=null){
				createAjaxBuilder(Form.FIELD_INPUT_CODE_SET).updatedFieldNames(Form.FIELD_CODES,Form.FIELD_SEPARATOR)
				.method(Boolean.class,new ListenValueMethod<Boolean>() {
					@Override
					public void execute(Boolean value) {
						onComplete(inputRowVisibility(form,Form.FIELD_CODES,value),inputRowVisibility(form,Form.FIELD_SEPARATOR,value));
					}
				}).build();
				
				createAjaxBuilder(Form.FIELD_INPUT_CODE_INTERVAL).updatedFieldNames(Form.FIELD_FROM_CODE,Form.FIELD_TO_CODE
						,Form.FIELD_CODE_STEP)
				.method(Boolean.class,new ListenValueMethod<Boolean>() {
					@Override
					public void execute(Boolean value) {
						onComplete(inputRowVisibility(form,Form.FIELD_FROM_CODE,value),inputRowVisibility(form,Form.FIELD_TO_CODE,value)
								,inputRowVisibility(form,Form.FIELD_CODE_STEP,value));
					}
				}).build();
			}
			
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_CODES,Boolean.FALSE));
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_SEPARATOR,Boolean.FALSE));
			
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_FROM_CODE,Boolean.FALSE));
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_TO_CODE,Boolean.FALSE));
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_CODE_STEP,Boolean.FALSE));
			*/
		}else if(Crud.UPDATE.equals(crud)){
			
		}
		
	}
	
	@Override
	protected void create() {
		if(Boolean.TRUE.equals(CREATE_ON_SALABLE_PRODUCT))
			super.create();
		else{
			inject(SalableProductInstanceBusiness.class).create(identifiable.getCollection(),((Form)form.getData()).codes.getCodeSet() );
		}
	}
	
	@Override
	protected SalableProductInstance instanciateIdentifiable() {
		SalableProductInstance salableProductInstance = super.instanciateIdentifiable();
		salableProductInstance.setCollection(identifiableFromRequestParameter(SalableProduct.class,uiManager.businessEntityInfos(SalableProduct.class).getIdentifier()));
		return salableProductInstance;
	}
		
	public static class Form extends AbstractFormModel<SalableProductInstance> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SalableProduct salableProduct;
		
		@IncludeInputs(layout=org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout.VERTICAL) private CodesFormModel codes = new CodesFormModel();
		
		@Override
		public void read() {
			super.read();
			salableProduct = identifiable.getCollection();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.setCollection(salableProduct);
		}
		
		/**/
		
		public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
		
	}

}
