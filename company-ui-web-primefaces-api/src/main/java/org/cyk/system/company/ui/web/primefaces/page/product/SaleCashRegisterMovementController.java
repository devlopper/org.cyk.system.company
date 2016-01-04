package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter
public class SaleCashRegisterMovementController extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1448560309988513836L;

	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	
	private SaleCashRegisterMovement saleCashRegisterMovement;
	private Boolean deposit,showIn,showOut,showBalance;
	private BigDecimal amountToHand,balance=BigDecimal.ZERO;

	public void init(SaleCashRegisterMovement saleCashRegisterMovement,Boolean deposit){
		this.saleCashRegisterMovement = saleCashRegisterMovement;
		this.deposit = deposit;
		showIn=Boolean.TRUE.equals(deposit);
		showOut=!showIn;
		showBalance=Boolean.TRUE;
	}
	
	public void amountInChanged(){
		saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
		amountToHand = saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getSale().getCost());
		balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
		showOut = BigDecimal.ZERO.compareTo(amountToHand)<0;
	}
	
	public void amountOutChanged(){
		saleCashRegisterMovementBusiness.out(saleCashRegisterMovement);
		balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
	}
	
}
