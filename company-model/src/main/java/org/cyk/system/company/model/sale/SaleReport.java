package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollectionReport;

@Getter @Setter @NoArgsConstructor
public class SaleReport extends AbstractSaleReport<SaleReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private BalanceReport balance = new BalanceReport();
	private CashRegisterMovementTermCollectionReport cashRegisterMovementTermCollection = new CashRegisterMovementTermCollectionReport();
	private Collection<SaleCashRegisterMovementReport> saleCashRegisterMovements = new ArrayList<>();

	@Override
	public void generate() {
		super.generate();
		balance.generate();
		for(int i = 0 ; i < 2 ; i++){
			SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();
			saleCashRegisterMovement.setSale(this);
			saleCashRegisterMovement.generate();
			saleCashRegisterMovements.add(saleCashRegisterMovement);
		}
	}
}
