package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalableProductCollectionItemBalanceReport extends AbstractIdentifiableReport<SalableProductCollectionItemBalanceReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SalableProductCollectionItemReport salableProductCollectionItem;
	private String amount;
	private BalanceReport balance = new BalanceReport();
	
	@Override
	public void generate() {
		super.generate();
		amount = provider.randomInt(1, 10000)+"";
		balance.generate();
	}
	
}
