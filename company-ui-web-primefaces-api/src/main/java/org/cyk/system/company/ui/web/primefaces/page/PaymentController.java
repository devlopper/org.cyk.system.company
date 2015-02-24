package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.model.product.Payment;

@Getter @Setter
public class PaymentController implements Serializable {

	private static final long serialVersionUID = 1448560309988513836L;

	private PaymentBusiness paymentBusiness;
	
	private Payment payment;
	private Boolean payback,showIn,showOut,showBalance;
	private BigDecimal amountToHand=BigDecimal.ZERO,balance=BigDecimal.ZERO;

	public PaymentController(PaymentBusiness paymentBusiness,Payment payment,Boolean payback) {
		this.paymentBusiness = paymentBusiness;
		this.payment = payment;
		this.payback = payback;
		showIn=!Boolean.TRUE.equals(payback);
		showOut=!showIn;
		showBalance=Boolean.TRUE;
	}
	
	public void amountInChanged(){
		paymentBusiness.in(payment);
		amountToHand = payment.getAmountIn().subtract(payment.getSale().getCost());
		//balance = payment.getAmountOut().subtract(amountToHand);
		//balance = (payment.getSale().getBalance()==null?payment.getSale().getCost():payment.getSale().getBalance()).subtract(payment.getAmountIn());
		balance = paymentBusiness.computeBalance(payment);
		
		showOut = BigDecimal.ZERO.compareTo(amountToHand)<0;
	}
	
	public void amountOutChanged(){
		paymentBusiness.out(payment);
		//balance = payment.getAmountOut().subtract(amountToHand);
		balance = paymentBusiness.computeBalance(payment);
	}
	
}
