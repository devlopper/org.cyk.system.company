package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
	
	private CostReport cost = new CostReport();
	
	private SalableProductCollectionItemBalanceReport currentSalableProductCollectionItemBalance;
	private SalableProductCollectionItemBalanceReport previousSalableProductCollectionItemBalance;
	
	private List<SalableProductCollectionItemBalanceReport> salableProductCollectionItemBalances = new ArrayList<>();
	
	@Override
	public void generate() {
		super.generate();
		salableProduct.generate();
		cost.generate();
		//balance.generate();
		quantity = provider.randomInt(1, 10)+"";
		reduction = provider.randomInt(1, 1000)+"";
		commission = provider.randomInt(1, 10000)+"";
		for(int i = 0 ; i < 2 ; i++){
			SalableProductCollectionItemBalanceReport salableProductCollectionItemBalanceReport = new SalableProductCollectionItemBalanceReport();
			salableProductCollectionItemBalanceReport.generate();
			salableProductCollectionItemBalanceReport.setSalableProductCollectionItem(this);
			salableProductCollectionItemBalances.add(salableProductCollectionItemBalanceReport);
		}
		//System.out.println("SalableProductCollectionItemReport.generate() : "+salableProductCollectionItemBalances.size());
		currentSalableProductCollectionItemBalance = getBalanceAtFromEnd(0);
		previousSalableProductCollectionItemBalance = getBalanceAtFromEnd(1);
	}
	
	public SalableProductCollectionItemBalanceReport getBalanceAtFromEnd(Integer index){
		if(salableProductCollectionItemBalances.size() <= index)
			return null;
		return salableProductCollectionItemBalances.get(salableProductCollectionItemBalances.size()-index-1);
	}
	
	public String getSalableProductCollectionItemBalancesAmount(){
		Collection<String> collection = new ArrayList<>();
		for(SalableProductCollectionItemBalanceReport salableProductCollectionItemBalance : salableProductCollectionItemBalances)
			collection.add(salableProductCollectionItemBalance.getAmount());
		return StringUtils.join(collection,",");
	}
	
}
