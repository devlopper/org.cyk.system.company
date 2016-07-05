package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceEditPage extends AbstractCrudOnePage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean CREATE_ON_SALABLE_PRODUCT = Boolean.TRUE;
	
	public static ControlSetAdapter<Object> CREATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		@Override
		public Boolean build(Field field) {
			if(Boolean.TRUE.equals(CREATE_ON_SALABLE_PRODUCT))
				return ArrayUtils.contains(new String[]{Form.FIELD_SALABLE_PRODUCT,Form.FIELD_CODE}, field.getName());
			else
				return !ArrayUtils.contains(new String[]{Form.FIELD_CODE}, field.getName());
		}
		
		public void input(org.cyk.ui.api.data.collector.form.ControlSet<Object,DynaFormModel,DynaFormRow,DynaFormLabel,DynaFormControl,SelectItem> controlSet, org.cyk.ui.api.data.collector.control.Input<?,DynaFormModel,DynaFormRow,DynaFormLabel,DynaFormControl,SelectItem> input) {
			
		}
	};
	
	public static ControlSetAdapter<Object> UPDATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		@Override
		public Boolean build(Field field) {
			return ArrayUtils.contains(new String[]{Form.FIELD_SALABLE_PRODUCT,Form.FIELD_CODE}, field.getName());
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
			Form _form = (Form)form.getData();
			Set<String> codeSet = new LinkedHashSet<>();
					
			String codes = StringUtils.remove(_form.codes, Constant.LINE_DELIMITER);
			String[] codesSplitted = StringUtils.split(codes, _form.separator);
			if(codesSplitted!=null)
				codeSet.addAll(Arrays.asList(codesSplitted));
			
			if(_form.fromCode!=null && _form.toCode!=null && _form.codeStep!=null)
				for(int index = _form.fromCode; index <= _form.toCode; index = index + _form.codeStep)
					codeSet.add(String.valueOf(index));
			
			CompanyBusinessLayer.getInstance().getSalableProductInstanceBusiness().create(identifiable.getCollection(),codeSet );
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
		@Input @InputText @NotNull private String code;
		
		//@Input @InputBooleanCheck @NotNull private Boolean inputCodeSet;
		@Input @InputTextarea /*@NotNull*/ private String codes;
		@Input @InputText /*@NotNull*/ private String separator;
		
		//@Input @InputBooleanCheck @NotNull private Boolean inputCodeInterval;
		@Input @InputNumber /*@NotNull*/ private Integer fromCode;
		@Input @InputNumber /*@NotNull*/ private Integer toCode;
		@Input @InputNumber /*@NotNull*/ private Integer codeStep=1;
		
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
		public static final String FIELD_INPUT_CODE_SET = "inputCodeSet";
		public static final String FIELD_CODE = "code";
		public static final String FIELD_CODES = "codes";
		public static final String FIELD_SEPARATOR = "separator";
		public static final String FIELD_INPUT_CODE_INTERVAL = "inputCodeInterval";
		public static final String FIELD_FROM_CODE = "fromCode";
		public static final String FIELD_TO_CODE = "toCode";
		public static final String FIELD_CODE_STEP = "codeStep";
	}

}
