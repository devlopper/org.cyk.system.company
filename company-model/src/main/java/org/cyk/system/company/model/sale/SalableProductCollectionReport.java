package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.CostReport;
import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SalableProductCollectionReport extends AbstractIdentifiableReport<SalableProductCollectionReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	private CostReport cost = new CostReport();
	private Collection<SalableProductCollectionItemReport> items = new ArrayList<>();
	
	private String totalAmountDu,totalAmountPaid;
	private BalanceReport totalBalance=new BalanceReport();
	
	@Override
	public void generate() {
		super.generate();
		accountingPeriod.generate();
		cost.generate();
		totalBalance.generate();
		totalAmountDu=provider.randomInt(1, 100)+"";
		totalAmountPaid=provider.randomInt(1, 100)+"";
		for(int i=0;i<6;i++){
			SalableProductCollectionItemReport item = new SalableProductCollectionItemReport();
			item.generate();
			item.setSalableProductCollection(this);
			items.add(item);
		}
		
	}
	
	
	
}
