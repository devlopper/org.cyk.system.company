package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementReport extends AbstractIdentifiableReport<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleCashRegisterMovementCollectionReport saleCashRegisterMovementCollection;
	private SaleReport sale = new SaleReport();
	private String amount;
	private BalanceReport balance = new BalanceReport();
	
	private List<SalableProductCollectionItemSaleCashRegisterMovementReport> salableProductCollectionItemSaleCashRegisterMovements = new ArrayList<>();
	
	public SaleCashRegisterMovementReport(SaleCashRegisterMovementCollectionReport saleCashRegisterMovementCollection,SaleCashRegisterMovement source) {
		this.saleCashRegisterMovementCollection = saleCashRegisterMovementCollection;
		setSource(source);
	}
		
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		System.out.println("SaleCashRegisterMovementReport.setSource() : "+((AbstractCollectionItem<?>) source).getCode());
		globalIdentifier.setCode(RootConstant.Code.getRelativeCode((AbstractCollectionItem<?>) source));
		sale.setSource(((SaleCashRegisterMovement)source).getSale());
		amount = format(((SaleCashRegisterMovement)source).getAmount());
		balance.setSource(((SaleCashRegisterMovement)source).getBalance());
		
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
	
	public Balance getPreviousBalanceIdentifiable(){
		if(previous==null){
			Balance balance = new Balance();
			balance.setValue(((Sale)sale.getSource()).getSalableProductCollection().getCost().getValue());
			return balance;
		}
		return ((SaleCashRegisterMovement)previous.getSource()).getBalance();
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
		sale.generate();
		amount=provider.randomInt(1, 1000000)+"";
		balance.generate();
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
