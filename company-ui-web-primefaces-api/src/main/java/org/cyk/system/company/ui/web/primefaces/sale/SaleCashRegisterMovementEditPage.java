package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.payment.AbstractCashRegisterMovementEditPage;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<SaleCashRegisterMovement,Sale> implements Serializable {

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
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		createAjaxBuilder(Form.FIELD_CASH_REGISTER)
		.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
			@Override
			public void execute(CashRegister cashRegister) {
				inject(SaleCashRegisterMovementBusiness.class).setCashRegister(userSession.getUserAccount(), identifiable, cashRegister);
				identifiable.setAmountIn(((Form)form.getData()).getValue());
			}
		}).build();
	}
	
	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable(Sale sale) {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount(),sale,webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable() {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount(), null
				, webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected void selectCollection(Sale sale) {
		super.selectCollection(sale);
		inject(SaleCashRegisterMovementBusiness.class).setSale(identifiable, sale);
		updateCurrentTotal();
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable.getCashRegisterMovement();
	}
	
	@Override
	protected MovementCollection getMovementCollection(Sale sale) {
		if(((Form)form.getData()).getCashRegister()==null)
			return null;
		return ((Form)form.getData()).getCashRegister().getMovementCollection();
	}
	
	@Override
	protected BigDecimal getCurrentTotal() {
		if(identifiable.getSale()==null)
			return null;
		return identifiable.getSale().getBalance().getValue();
	}
	
	@Override
	protected BigDecimal getNextTotal(BigDecimal increment) {
		if(identifiable.getSale()==null)
			return null;
		return inject(SaleCashRegisterMovementBusiness.class).computeBalance(identifiable,(MovementAction) form.findInputByFieldName(Form.FIELD_ACTION).getValue()
				,increment);
	}
		
	/**/
	
	@Getter @Setter @FieldOverride(name=AbstractCashRegisterMovementForm.FIELD_COLLECTION,type=Sale.class)
	public static class Form extends AbstractCashRegisterMovementForm<SaleCashRegisterMovement,Sale> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
				
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable.getCashRegisterMovement();
		}
		
		@Override
		public void read() {
			cashRegister = identifiable.getCashRegisterMovement().getCashRegister();
			super.read();
		}
		
		@Override
		public void write() {
			identifiable.getCashRegisterMovement().setCashRegister(cashRegister);
			identifiable.getCashRegisterMovement().getMovement().setCollection(cashRegister.getMovementCollection());
			super.write();
			identifiable.setAmountIn(getCashRegisterMovement().getMovement().getValue());
			identifiable.setAmountOut(BigDecimal.ZERO);
		}
		
		/**/
		
		public static final String FIELD_CASH_REGISTER = "cashRegister";

		@Override
		protected CashRegister getCashRegister() {
			return cashRegister;
		}
		
	}
	

}