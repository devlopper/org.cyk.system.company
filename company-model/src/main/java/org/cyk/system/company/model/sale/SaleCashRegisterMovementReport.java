package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.cyk.system.company.model.payment.CashRegisterMovementReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleCashRegisterMovementReport extends AbstractIdentifiableReport<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport sale;
	private CashRegisterMovementReport cashRegisterMovement = new CashRegisterMovementReport();
	
	private String amountIn,amountToOut,amountOut,balance;

	@Override
	public void generate() {
		super.generate();
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		balance=provider.randomInt(1, 1000000)+"";
		cashRegisterMovement.generate();
	}
	
}
