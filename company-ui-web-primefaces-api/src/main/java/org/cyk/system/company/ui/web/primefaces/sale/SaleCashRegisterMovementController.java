package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashRegisterMovementModeBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter @Deprecated
public class SaleCashRegisterMovementController extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1448560309988513836L;

	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	
	private SaleCashRegisterMovement saleCashRegisterMovement;
	private List<CashRegisterMovementMode> modes;
	private List<SalableProductInstance> salableProductInstances;
	private Boolean deposit,showMode=Boolean.TRUE,showDocumentIdentifier,showIn,showOut,showBalance,showSalableProductInstances;
	private BigDecimal amountToHand,balance=BigDecimal.ZERO;

	public void init(SaleCashRegisterMovement saleCashRegisterMovement,Boolean deposit){
		this.saleCashRegisterMovement = saleCashRegisterMovement;
		this.deposit = deposit;
		showIn=Boolean.TRUE.equals(deposit);
		showOut=!showIn;
		showBalance=Boolean.TRUE;
		if(modes==null)
			modes = new ArrayList<>(inject(CashRegisterMovementModeBusiness.class).findAll());
	}
	
	public void modeChangedListener(ValueChangeEvent event){
		CashRegisterMovementMode cashRegisterMovementMode = (CashRegisterMovementMode)event.getNewValue();
		if(cashRegisterMovementMode==null){
			showDocumentIdentifier = Boolean.FALSE;
		}else{
			showDocumentIdentifier = Boolean.TRUE.equals(cashRegisterMovementMode.getSupportDocumentIdentifier());
			showSalableProductInstances = CompanyConstant.Code.CashRegisterMovementMode.GIFT_CARD.equals(cashRegisterMovementMode.getCode());
		}
	}
	
	public void amountInChanged(){
		//saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
		//amountToHand = saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue());
		//balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
		showOut = BigDecimal.ZERO.compareTo(amountToHand)<0;
	}
	
	public void amountOutChanged(){
		//saleCashRegisterMovementBusiness.out(saleCashRegisterMovement);
		//balance = saleCashRegisterMovementBusiness.computeBalance(saleCashRegisterMovement);
	}
	
	
	
}
