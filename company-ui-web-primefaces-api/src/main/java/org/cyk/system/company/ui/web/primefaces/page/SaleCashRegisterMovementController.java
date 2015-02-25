package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter
public class SaleCashRegisterMovementController extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1448560309988513836L;

	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private CashierBusiness cashierBusiness;
	
	private Cashier cashier;
	private SaleCashRegisterMovement saleCashRegisterMovement;
	private Boolean deposit,showIn,showOut,showBalance;
	private BigDecimal amountIn,amountOut,amountToHand,balance=BigDecimal.ZERO;

	@Override
	protected void initialisation() {
		super.initialisation();
		cashier = cashierBusiness.find().one();//Must be found by employee / user session
	}
	
	public void init(SaleCashRegisterMovement saleCashRegisterMovement,Boolean deposit){
		this.saleCashRegisterMovement = saleCashRegisterMovement;
		this.deposit = deposit;
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashier.getCashRegister());
		showIn=!Boolean.TRUE.equals(deposit);
		showOut=!showIn;
		showBalance=Boolean.TRUE;
		amountIn = amountOut = amountToHand=BigDecimal.ZERO;
	}
	
	public void amountInChanged(){
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(amountIn);
		amountToHand = amountIn.subtract(saleCashRegisterMovement.getSale().getCost());
		balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
		
		showOut = BigDecimal.ZERO.compareTo(amountToHand)<0;
	}
	
	public void amountOutChanged(){
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(amountIn.subtract(amountOut));
		balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
	}
	
}
