package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.payment.AbstractCashRegisterMovementEditPage;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.mathematics.movement.MovementAction;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementEditPage extends AbstractCashRegisterMovementEditPage<SaleCashRegisterMovement,Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
		
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		/*createAjaxBuilder(Form.FIELD_CASH_REGISTER)
		.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
			@Override
			public void execute(CashRegister cashRegister) {
				if(Crud.CREATE.equals(crud))
					inject(SaleCashRegisterMovementBusiness.class).setCashRegister(userSession.getUserAccount(), identifiable, cashRegister);
				identifiable.setAmountIn(((Form)form.getData()).getValue());
				((Form)form.getData()).setMovement(identifiable.getCashRegisterMovement().getMovement());
			}
		}).build();
		*/
	}
	
	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable(Sale sale) {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount(),sale,webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected SaleCashRegisterMovement instanciateIdentifiable() {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount()
				, webManager.getIdentifiableFromRequestParameter(Sale.class,Boolean.TRUE)
				, webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected Sale getCollection(SaleCashRegisterMovement saleCashRegisterMovement) {
		return saleCashRegisterMovement.getSale();
	}
	
	@Override
	protected void selectCollection(Sale sale) {
		super.selectCollection(sale);
		if(Crud.CREATE.equals(crud))
			inject(SaleCashRegisterMovementBusiness.class).setSale(identifiable, sale);
		updateCurrentTotal();
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable.getCollection().getCashRegisterMovement();
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
		return null;//identifiable.getSale().getBalance().getValue();
	}
	
	@Override
	protected BigDecimal getNextTotal(BigDecimal increment) {
		if(identifiable.getSale()==null)
			return null;
		return inject(SaleCashRegisterMovementBusiness.class).computeBalance(identifiable,(MovementAction) form.getInputByFieldName(Form.FIELD_ACTION).getValue()
				,increment == null ? BigDecimal.ZERO : increment);
	}
		
	/**/
	
	@Getter @Setter @FieldOverride(name=AbstractCashRegisterMovementForm.FIELD_COLLECTION,type=Sale.class)
	public static class Form extends AbstractCashRegisterMovementForm<SaleCashRegisterMovement,Sale> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
				
		@Override
		protected CashRegisterMovement getCashRegisterMovement() {
			return identifiable.getCollection().getCashRegisterMovement();
		}
		
		@Override
		public void read() {
			cashRegister = identifiable.getCollection().getCashRegisterMovement().getCashRegister();
			super.read();
		}
		
		@Override
		public void write() {
			identifiable.getCollection().getCashRegisterMovement().setCashRegister(cashRegister);
			identifiable.getCollection().getCashRegisterMovement().getMovement().setCollection(cashRegister.getMovementCollection());
			super.write();
			identifiable.getCollection().setAmountIn(getCashRegisterMovement().getMovement().getValue());
			identifiable.getCollection().setAmountOut(BigDecimal.ZERO);
		}
		
		/**/
		
		public static final String FIELD_CASH_REGISTER = "cashRegister";

		@Override
		protected CashRegister getCashRegister() {
			return cashRegister;
		}
		
	}
	

}