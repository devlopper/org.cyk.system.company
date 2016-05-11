package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.payment.AbstractCashRegisterMovementEditPage;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.network.UniformResourceLocatorParameter;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.web.api.data.collector.control.WebInput;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<SaleCashRegisterMovement> implements Serializable {

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
		form.findInputByFieldName(Form.FIELD_SALE).setDisabled(identifiable.getSale()!=null);
		addInputListener(Form.FIELD_VALUE, new WebInput.Listener.Adapter(){
			private static final long serialVersionUID = 7526066306750441853L;
			@Override
			public void validate(FacesContext facesContext,UIComponent uiComponent, Object value) throws ValidatorException {
				BigDecimal total = computeNextTotal((BigDecimal) value);
				if(total.signum() == -1)
					webManager.throwValidationException("invalidvalue", new Object[]{});
			}
		});
	}
		
	@Override
	protected Boolean showCashRegisterField() {
		return Boolean.FALSE;
	}

	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable() {
		/*Sale sale = null;
		Long saleIdentifier = requestParameterLong(Sale.class);
		if(saleIdentifier==null)
			;
		else
			sale = CompanyBusinessLayer.getInstance().getSaleBusiness().find(saleIdentifier);
		*/
		Sale sale = webManager.getIdentifiableFromRequestParameter(Sale.class, Boolean.TRUE);
		//identifiable.setSale();
		SaleCashRegisterMovement saleCashRegisterMovement = 
				CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().instanciateOne(sale, (Person) userSession.getUser(), Boolean.TRUE);
		String action = requestParameter(UniformResourceLocatorParameter.ACTION_IDENTIFIER);
		if(CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementInput().equals(action))
			saleCashRegisterMovement.getCashRegisterMovement().getMovement().setAction(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction());
		else if(CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementOutput().equals(action))
			saleCashRegisterMovement.getCashRegisterMovement().getMovement().setAction(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction());
		else
			saleCashRegisterMovement.getCashRegisterMovement().getMovement().setAction(null);
		return saleCashRegisterMovement;
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable.getCashRegisterMovement();
	}
	
	@Override
	protected BigDecimal getCurrentTotal() {
		return identifiable.getSale().getBalance().getValue();
	}
	
	@Override
	protected BigDecimal computeNextTotal(BigDecimal increment) {
		return CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().computeBalance(identifiable,(MovementAction) form.findInputByFieldName(Form.FIELD_ACTION).getValue()
				,increment);
	}
		
	/**/
	
	public static class Form extends AbstractCashRegisterMovementForm<SaleCashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Sequence(direction=Direction.BEFORE,field=AbstractCashRegisterMovementForm.FIELD_CURRENT_TOTAL)
		@Input(readOnly=true) @InputChoice @InputOneChoice @InputOneCombo @NotNull private Sale sale;
				
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable.getCashRegisterMovement();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.setAmountIn(getCashRegisterMovement().getMovement().getValue());
			identifiable.setAmountOut(BigDecimal.ZERO);
		}
		
		/**/
		
		public static final String FIELD_SALE = "sale";
		
	}
	

}