package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.BalanceFormModel;
import org.cyk.system.company.ui.web.primefaces.payment.AbstractCashRegisterMovementEditPage.AbstractCashRegisterMovementForm;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionItemSaleCashRegisterMovementEditPage extends AbstractCrudOnePage<SalableProductCollectionItemSaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		/*if(identifiable.getSale()==null){
			identifiable.setSale(saleBusiness.find(requestParameterLong(uiManager.keyFromClass(Sale.class))));
			//identifiable.setCashRegisterMovement(new CashRegisterMovement(cashierBusiness.findByPerson((Person) getUserSession().getUser()).getCashRegister()));
		}
		cashRegisterController.init(identifiable,!StringUtils.equals(CompanyWebManager.getInstance().getRequestParameterPayback(), 
				requestParameter(CompanyWebManager.getInstance().getRequestParameterPaymentType())));
		*/
		
		//if(identifiable.getSale()==null)
		//	identifiable.setSale(webManager.getIdentifiableFromRequestParameter(Sale.class, Boolean.TRUE));
	}
		
	/*@Override
	protected String buildContentTitle() {
		String string =  super.buildContentTitle();
		if(Crud.CREATE.equals(crud))
			if(getCashRegisterMovement().getMovement().getAction().equals(getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction()))
				string += Constant.CHARACTER_SLASH+text("command.pay");
			else if(getCashRegisterMovement().getMovement().getAction().equals(getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction()))
				string += Constant.CHARACTER_SLASH+text("command.payback");
		return string;
	}*/
		
	/**/
	
	@Getter @Setter @FieldOverride(name=AbstractCashRegisterMovementForm.FIELD_COLLECTION,type=Sale.class)
	public static class Form extends AbstractFormModel<SalableProductCollectionItemSaleCashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SalableProductCollectionItem salableProductCollectionItem;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SaleCashRegisterMovement saleCashRegisterMovement;
		@Input @InputNumber @NotNull private BigDecimal amount;
		@IncludeInputs private BalanceFormModel balance = new BalanceFormModel();
				
		@Override
		public void read() {
			super.read();
		}
		
		@Override
		public void write() {
			super.write();
			
		}
		
		/**/
		
		public static final String FIELD_SALABLE_PRODUCT_COLLECTION_ITEM = "salableProductCollectionItem";
		public static final String FIELD_SALE_CASH_REGISTER_MOVEMENT = "saleCashRegisterMovement";
		public static final String FIELD_AMOUNT = "amount";
		public static final String FIELD_BALANCE = "balance";
		
	}
	

}