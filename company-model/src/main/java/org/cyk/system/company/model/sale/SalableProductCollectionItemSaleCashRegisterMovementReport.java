package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalableProductCollectionItemSaleCashRegisterMovementReport extends AbstractIdentifiableReport<SalableProductCollectionItemSaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SalableProductCollectionItemReport salableProductCollectionItem;
	private SaleCashRegisterMovementReport saleCashRegisterMovement;
	private String amount;
	private BalanceReport balance = new BalanceReport();
	
	public SalableProductCollectionItemSaleCashRegisterMovementReport(SalableProductCollectionItemReport salableProductCollectionItem,SaleCashRegisterMovementReport saleCashRegisterMovement,SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement){
		this.salableProductCollectionItem = salableProductCollectionItem;
		this.saleCashRegisterMovement = saleCashRegisterMovement;
		setSource(salableProductCollectionItemSaleCashRegisterMovement);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		amount = format( ((SalableProductCollectionItemSaleCashRegisterMovement)source).getAmount());
		balance.setSource(((SalableProductCollectionItemSaleCashRegisterMovement)source).getBalance());
	}
	
	public BalanceReport getPreviousBalance(){
		if(previous==null){
			BalanceReport balance = new BalanceReport();
			balance.setValue(salableProductCollectionItem.getCost().getValue());
			return balance;
		}
		return previous.getBalance();
	}
	
	@Override
	public void generate() {
		super.generate();
		amount = provider.randomInt(1, 10000)+"";
		balance.generate();
	}
	
}
