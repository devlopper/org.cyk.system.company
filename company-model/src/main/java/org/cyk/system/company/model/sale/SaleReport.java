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
	
	private String totalAmountDu,totalAmountPaid;//Is it really used ???
	
	public SaleReport(Sale sale){
		setSource(sale);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		balance.setSource(((Sale)source).getBalance());
		
	}
	
	@Override
	public void generate() {
		super.generate();
		balance.generate();
		totalAmountDu=provider.randomInt(1, 100)+"";
		totalAmountPaid=provider.randomInt(1, 100)+"";
		/*for(int i = 0 ; i < 2 ; i++){
			SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();
			saleCashRegisterMovement.setSale(this);
			saleCashRegisterMovement.generate();
			saleCashRegisterMovements.add(saleCashRegisterMovement);
		}*/
	}
}
