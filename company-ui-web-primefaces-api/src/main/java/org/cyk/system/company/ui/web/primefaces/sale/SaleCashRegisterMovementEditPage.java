package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.payment.AbstractCashRegisterMovementEditPage;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<SaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private CashierBusiness cashierBusiness;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	
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
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		createAjaxBuilder(Form.FIELD_SALE)/*.crossedFieldNames("number2")*/.updatedFieldNames(Form.FIELD_INITIAL_BALANCE).method(Sale.class,new ListenValueMethod<Sale>() {
			@Override
			public void execute(Sale sale) {
				setFieldValue(Form.FIELD_INITIAL_BALANCE, sale.getBalance().getValue());
			}
		}).build();
	}
	
	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable() {
		Sale sale = null;
		Long saleIdentifier = requestParameterLong(Sale.class);
		if(saleIdentifier==null)
			;
		else
			sale = CompanyBusinessLayer.getInstance().getSaleBusiness().find(saleIdentifier);
		return CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().instanciate(sale, (Person) userSession.getUser(), Boolean.TRUE);
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable.getCashRegisterMovement();
	}
	
	@Override
	protected void create() {
		super.create();
	}
	
	/**/
	
	public static class Form extends AbstractCashRegisterMovementForm<SaleCashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Sale sale;
		
		//@Input(readOnly=true) @InputNumber private BigDecimal initialBalance;
		//@Input(readOnly=true) @InputText private String initialBalance;
		@Input(disabled=true) @InputNumber @NotNull private BigDecimal initialBalance;
		@Input(disabled=true) @InputNumber @NotNull private BigDecimal finalBalance;
		
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable.getCashRegisterMovement();
		}
		
		/**/
		
		public static final String FIELD_SALE = "sale";
		public static final String FIELD_INITIAL_BALANCE = "initialBalance";
		public static final String FIELD_FINAL_BALANCE = "finalBalance";
	}
	

}