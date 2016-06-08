package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;

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
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceEditPage extends AbstractCrudOnePage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean CREATE_ON_SALABLE_PRODUCT = Boolean.TRUE;
	
	public static ControlSetAdapter<Object> CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		@Override
		public Boolean build(Field field) {
			if(Boolean.TRUE.equals(CREATE_ON_SALABLE_PRODUCT))
				return ArrayUtils.contains(new String[]{Form.FIELD_SALABLE_PRODUCT,Form.FIELD_CODE}, field.getName());
			else
				return ArrayUtils.contains(new String[]{Form.FIELD_SALABLE_PRODUCT,Form.FIELD_CODES,Form.FIELD_SEPARATOR}, field.getName());
		}
	};
	
	@Override
	protected void initialisation() {
		super.initialisation();
		if(CREATE_ON_SALABLE_PRODUCT!=null)
			form.getControlSetListeners().add(CONTROL_SET_ADAPTER);
	}
	
	@Override
	protected void create() {
		if(Boolean.TRUE.equals(CREATE_ON_SALABLE_PRODUCT))
			super.create();
		else{
			String codes = StringUtils.remove(((Form)form.getData()).codes, Constant.LINE_DELIMITER);
			CompanyBusinessLayer.getInstance().getSalableProductInstanceBusiness().create(identifiable.getCollection()
					,new LinkedHashSet<>(Arrays.asList(StringUtils.split(codes, ((Form)form.getData()).separator))) );
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
		@Input @InputTextarea @NotNull private String codes;
		@Input @InputText @NotNull private String separator;
		
		@Override
		public void read() {
			super.read();
			salableProduct = identifiable.getCollection();
		}
		
		/**/
		
		public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
		public static final String FIELD_CODE = "code";
		public static final String FIELD_CODES = "codes";
		public static final String FIELD_SEPARATOR = "separator";
	}

}
