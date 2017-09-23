package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.company.model.payment.CashRegisterMovementReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementCollectionReport extends AbstractIdentifiableReport<SaleCashRegisterMovementCollectionReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	private CashRegisterMovementReport cashRegisterMovement = new CashRegisterMovementReport();
	private String amountIn,amountToOut,amountOut;
	
	private List<SaleCashRegisterMovementReport> saleCashRegisterMovements = new ArrayList<>();
	
	public SaleCashRegisterMovementCollectionReport(SaleCashRegisterMovementCollection source) {
		setSource(source);
	}
	
	public BalanceReport getBalance(){
		BalanceReport balanceReport = new BalanceReport();
		Balance balance = new Balance();
		for(SaleCashRegisterMovementReport saleCashRegisterMovement : saleCashRegisterMovements){
			commonUtils.increment(BigDecimal.class, balance, Balance.FIELD_VALUE, ((SaleCashRegisterMovement)saleCashRegisterMovement.getSource()).getBalance().getValue());
		}
		balanceReport.setSource(balance);
		return balanceReport;
	}
	
	public BalanceReport getPreviousBalance(){
		BalanceReport balanceReport = new BalanceReport();
		Balance balance = new Balance();
		for(SaleCashRegisterMovementReport saleCashRegisterMovement : saleCashRegisterMovements){
			commonUtils.increment(BigDecimal.class, balance, Balance.FIELD_VALUE, saleCashRegisterMovement.getPreviousBalanceIdentifiable().getValue());
		}
		balanceReport.setSource(balance);
		return balanceReport;
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		accountingPeriod.setSource(((SaleCashRegisterMovementCollection)source).getAccountingPeriod());
		cashRegisterMovement.setSource(((SaleCashRegisterMovementCollection)source).getCashRegisterMovement());
		amountIn = format(((SaleCashRegisterMovementCollection)source).getAmountIn());
		amountOut = format(((SaleCashRegisterMovementCollection)source).getAmountOut());
		for(SaleCashRegisterMovement index : ((SaleCashRegisterMovementCollection)source).getItems().getElements()){
			saleCashRegisterMovements.add(new SaleCashRegisterMovementReport(this,index));
		}
	}
	
	@Override
	public void generate() {
		super.generate();
		accountingPeriod.generate();
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		cashRegisterMovement.generate();
		for(int i = 0 ; i < 3 ; i++){
			SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();
			saleCashRegisterMovement.setSaleCashRegisterMovementCollection(this);
			saleCashRegisterMovement.generate();
			
			SaleCashRegisterMovementReport previousSaleCashRegisterMovement = new SaleCashRegisterMovementReport();
			SaleCashRegisterMovement previousSaleCashRegisterMovementSource = new SaleCashRegisterMovement();
			previousSaleCashRegisterMovementSource.getBalance().setValue(new BigDecimal(provider.randomInt(1, 1000000)));
			saleCashRegisterMovement.setPrevious(previousSaleCashRegisterMovement);
			previousSaleCashRegisterMovement.setSourceOnly(previousSaleCashRegisterMovementSource);
			previousSaleCashRegisterMovement.generate();
			
			saleCashRegisterMovements.add(saleCashRegisterMovement);
		}
	}
	
}
