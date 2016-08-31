package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.CostReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalableProductCollectionItemReport extends AbstractIdentifiableReport<SalableProductCollectionItemReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SalableProductCollectionReport salableProductCollection;
	private SalableProductReport salableProduct = new SalableProductReport();
	private String quantity;
	private String reduction;
	private String commission;
	
	private CostReport cost = new CostReport();

	@Override
	public void generate() {
		super.generate();
		salableProduct.generate();
		cost.generate();
		quantity = provider.randomInt(1, 10)+"";
		reduction = provider.randomInt(1, 1000)+"";
		commission = provider.randomInt(1, 10000)+"";
	}
	
}
