package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.payment.CashRegisterMovementReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementReport extends AbstractIdentifiableReport<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport sale;
	private CashRegisterMovementReport cashRegisterMovement = new CashRegisterMovementReport();
	
	private String amountIn,amountToOut,amountOut;
	private BalanceReport balance = new BalanceReport();
	
	private List<SalableProductCollectionItemSaleCashRegisterMovementReport> salableProductCollectionItemSaleCashRegisterMovements = new ArrayList<>();
	
	public SaleCashRegisterMovementReport(SaleReport sale,SaleCashRegisterMovement source) {
		this.sale = sale;
		setSource(source);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		cashRegisterMovement.setSource(((SaleCashRegisterMovement)source).getCollection().getCashRegisterMovement());
		balance.setSource(((SaleCashRegisterMovement)source).getBalance());
		amountIn = format(((SaleCashRegisterMovement)source).getCollection().getAmountIn());
		amountOut = format(((SaleCashRegisterMovement)source).getCollection().getAmountOut());
		
		for(SalableProductCollectionItemSaleCashRegisterMovement index : ((SaleCashRegisterMovement)source).getSalableProductCollectionItemSaleCashRegisterMovements().getCollection()){
			SalableProductCollectionItemSaleCashRegisterMovementReport salableProductCollectionItemSaleCashRegisterMovement
				= new SalableProductCollectionItemSaleCashRegisterMovementReport();
			SalableProductCollectionItemReport salableProductCollectionItem = new SalableProductCollectionItemReport();
			salableProductCollectionItem.setSource(index.getSalableProductCollectionItem());
			salableProductCollectionItemSaleCashRegisterMovement.setSalableProductCollectionItem(salableProductCollectionItem);
			salableProductCollectionItemSaleCashRegisterMovement.setSaleCashRegisterMovement(this);
			salableProductCollectionItemSaleCashRegisterMovement.setSource(index);
			salableProductCollectionItemSaleCashRegisterMovements.add(salableProductCollectionItemSaleCashRegisterMovement);
		}
	}
	
	public BalanceReport getPreviousBalance(){
		if(previous==null){
			BalanceReport balance = new BalanceReport();
			balance.setValue(sale.getSalableProductCollection().getCost().getValue());
			return balance;
		}
		return previous.getBalance();
	}
	
	@Override
	public void generate() {
		super.generate();
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		balance.generate();
		cashRegisterMovement.generate();
		for(SalableProductCollectionItemReport salableProductCollectionItem : sale.getSalableProductCollection().getItems()){
			SalableProductCollectionItemSaleCashRegisterMovementReport salableProductCollectionItemSaleCashRegisterMovement
				= new SalableProductCollectionItemSaleCashRegisterMovementReport();
			salableProductCollectionItemSaleCashRegisterMovement.setSalableProductCollectionItem(salableProductCollectionItem);
			salableProductCollectionItemSaleCashRegisterMovement.setSaleCashRegisterMovement(this);
			salableProductCollectionItemSaleCashRegisterMovement.generate();
			
			SalableProductCollectionItemSaleCashRegisterMovementReport previousSalableProductCollectionItemSaleCashRegisterMovement
				= new SalableProductCollectionItemSaleCashRegisterMovementReport();
			salableProductCollectionItemSaleCashRegisterMovement.setPrevious(previousSalableProductCollectionItemSaleCashRegisterMovement);
			previousSalableProductCollectionItemSaleCashRegisterMovement.generate();
			
			salableProductCollectionItemSaleCashRegisterMovements.add(salableProductCollectionItemSaleCashRegisterMovement);
		}
	}
	
}
