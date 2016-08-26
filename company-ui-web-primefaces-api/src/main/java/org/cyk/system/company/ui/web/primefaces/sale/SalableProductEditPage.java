package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class SalableProductEditPage extends AbstractCrudOnePage<SalableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean CREATE_ON_PRODUCT = Boolean.TRUE;
	
	public static ControlSetAdapter<Object> CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean build(Object data,Field field) {
			if(Boolean.TRUE.equals(CREATE_ON_PRODUCT))
				return ArrayUtils.contains(new String[]{Form.FIELD_PRODUCT,Form.FIELD_PRICE}, field.getName());
			else
				return ArrayUtils.contains(new String[]{Form.FIELD_NAME,Form.FIELD_CODE,Form.FIELD_PRICE}, field.getName());
		}
	};
	
	@Override
	protected void initialisation() {
		super.initialisation();
		if(CONTROL_SET_ADAPTER!=null)
			form.getControlSetListeners().add(CONTROL_SET_ADAPTER);
	}
	
	@Override
	protected void create() {
		if(Boolean.TRUE.equals(CREATE_ON_PRODUCT))
			super.create();
		else
			inject(SalableProductBusiness.class).create(TangibleProduct.class, ((Form)form.getData()).code, ((Form)form.getData()).name, ((Form)form.getData()).price);
	}
		
	public static class Form extends AbstractFormModel<SalableProduct> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Product product;
		@Input @InputText @NotNull private String code;
		@Input @InputText @NotNull private String name;
		@Input @InputNumber private BigDecimal price;
		
		/**/
		
		public static final String FIELD_PRODUCT = "product";
		public static final String FIELD_CODE = "code";
		public static final String FIELD_NAME = "name";
		public static final String FIELD_PRICE = "price";
	}

}
