package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleCashRegisterMovementReport extends AbstractSaleReport<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport sale = new SaleReport();
	private String amountIn,amountToOut,amountOut,balance;

	@Override
	public void generate() {
		super.generate();
		title = "Paiement";
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		balance=provider.randomInt(1, 1000000)+"";
		
		sale.generate();
	}
	
}
