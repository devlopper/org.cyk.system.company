package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.CostReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalableProductCollectionItemReport extends AbstractIdentifiableReport<SalableProductCollectionItemReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SalableProductCollectionReport salableProductCollection;
	private SalableProductReport salableProduct = new SalableProductReport();
	private String quantity;
	private String reduction;
	private String commission;
	private String amountPaid;
	
	private CostReport cost = new CostReport();
	private String costValueWithoutReduction;
	
	private BalanceReport balance = new BalanceReport();
	
	private List<SalableProductCollectionItemSaleCashRegisterMovementReport> salableProductCollectionItemSaleCashRegisterMovements = new ArrayList<>();
	
	public SalableProductCollectionItemReport(SalableProductCollectionReport salableProductCollection,SalableProductCollectionItem salableProductCollectionItem){
		this.salableProductCollection = salableProductCollection;
		setSource(salableProductCollectionItem);
		
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		salableProduct.setSource(((SalableProductCollectionItem)source).getSalableProduct());
		cost.setSource(((SalableProductCollectionItem)source).getCost());
		costValueWithoutReduction = format( ((SalableProductCollectionItem)source).getQuantifiedPrice());
		quantity = format(((SalableProductCollectionItem)source).getQuantity());
		//reduction = format(((SalableProductCollectionItem)source).getReduction());
		//commission = format(((SalableProductCollectionItem)source).getCommission());
		balance.setSource(((SalableProductCollectionItem)source).getBalance());
		for(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement : ((SalableProductCollectionItem)source).getSalableProductCollectionItemSaleCashRegisterMovements().getElements())
			salableProductCollectionItemSaleCashRegisterMovements.add(new SalableProductCollectionItemSaleCashRegisterMovementReport(this, null, salableProductCollectionItemSaleCashRegisterMovement));
	}
	
	@Override
	public void generate() {
		super.generate();
		salableProduct.generate();
		cost.generate();
		//balance.generate();
		quantity = provider.randomInt(1, 10)+"";
		reduction = provider.randomInt(1, 1000)+"";
		commission = provider.randomInt(1, 10000)+"";
		balance.generate();
		costValueWithoutReduction = provider.randomInt(1, 10000)+"";
		for(int i = 0 ; i < 2 ; i++){
			SalableProductCollectionItemSaleCashRegisterMovementReport salableProductCollectionItemSaleCashRegisterMovement = new SalableProductCollectionItemSaleCashRegisterMovementReport();
			salableProductCollectionItemSaleCashRegisterMovement.generate();
			salableProductCollectionItemSaleCashRegisterMovement.setSalableProductCollectionItem(this);
			salableProductCollectionItemSaleCashRegisterMovements.add(salableProductCollectionItemSaleCashRegisterMovement);
		}
	}
	
	public SalableProductCollectionItemSaleCashRegisterMovementReport getSaleCashRegisterMovementAtFromEnd(Integer index){
		if(salableProductCollectionItemSaleCashRegisterMovements.size() <= index)
			return null;
		return salableProductCollectionItemSaleCashRegisterMovements.get(salableProductCollectionItemSaleCashRegisterMovements.size()-index-1);
	}
	
	public String getSalableProductCollectionItemBalancesAmount(){
		Collection<String> collection = new ArrayList<>();
		for(SalableProductCollectionItemSaleCashRegisterMovementReport salableProductCollectionItemSaleCashRegisterMovement : salableProductCollectionItemSaleCashRegisterMovements)
			collection.add(salableProductCollectionItemSaleCashRegisterMovement.getAmount());
		return StringUtils.join(collection,",");
	}
	
}
