package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.CodesFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceCashRegisterEditPage extends AbstractCrudOnePage<SalableProductInstanceCashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean CREATE_ON_CASH_REGISTER = Boolean.TRUE;
	
	public static ControlSetAdapter<Object> CREATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean build(Object data,Field field) {
			if(Boolean.TRUE.equals(CREATE_ON_CASH_REGISTER))
				return ArrayUtils.contains(new String[]{Form.FIELD_CASH_REGISTER,Form.FIELD_SALABLE_PRODUCT_INSTANCE,Form.FIELD_FINITE_STATE_MACHINE_STATE}, field.getName());
			else
				return !ArrayUtils.contains(new String[]{Form.FIELD_SALABLE_PRODUCT_INSTANCE,CodesFormModel.FIELD_CODE,Form.FIELD_FINITE_STATE_MACHINE_STATE}, field.getName());
		}
	};
	
	public static ControlSetAdapter<Object> UPDATE_CONTROL_SET_ADAPTER = new ControlSetAdapter<Object>(){
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean build(Object data,Field field) {
			return ArrayUtils.contains(new String[]{Form.FIELD_CASH_REGISTER,Form.FIELD_SALABLE_PRODUCT_INSTANCE,Form.FIELD_FINITE_STATE_MACHINE_STATE}, field.getName());
		}
	};
	
	@Override
	protected void initialisation() {
		super.initialisation();
		if(Crud.CREATE.equals(crud)){
			if(CREATE_ON_CASH_REGISTER!=null)
				form.getControlSetListeners().add(CREATE_CONTROL_SET_ADAPTER);
		}else if(Crud.UPDATE.equals(crud)){
			form.getControlSetListeners().add(UPDATE_CONTROL_SET_ADAPTER);
		}
	}
	
	@Override
	protected void create() {
		if(Boolean.TRUE.equals(CREATE_ON_CASH_REGISTER))
			inject(SalableProductInstanceCashRegisterBusiness.class).create(identifiable);
		else{
			identifiable.getCashRegister().setProcessing(identifiable.getProcessing());
			inject(SalableProductInstanceCashRegisterBusiness.class).create(((Form)form.getData()).codes.getCodeSet(), identifiable.getCashRegister());
		}
	}
	
	@Override
	protected void update() {
		inject(SalableProductInstanceCashRegisterBusiness.class).update(identifiable);
	}
	
	@Override
	protected SalableProductInstanceCashRegister instanciateIdentifiable() {
		SalableProductInstanceCashRegister salableProductInstanceCashRegister = super.instanciateIdentifiable();
		//salableProductInstanceCashRegister.setCollection(identifiableFromRequestParameter(SalableProduct.class,uiManager.businessEntityInfos(SalableProduct.class).getIdentifier()));
		return salableProductInstanceCashRegister;
	}
		
	public static class Form extends AbstractFormModel<SalableProductInstanceCashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SalableProductInstance salableProductInstance;
		@IncludeInputs(layout=org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout.VERTICAL) private CodesFormModel codes = new CodesFormModel();
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private FiniteStateMachineState finiteStateMachineState;
		
		/**/
		
		public static final String FIELD_SALABLE_PRODUCT_INSTANCE = "salableProductInstance";
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_FINITE_STATE_MACHINE_STATE = "finiteStateMachineState";
	}

}
