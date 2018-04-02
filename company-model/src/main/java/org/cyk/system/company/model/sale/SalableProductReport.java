package org.cyk.system.company.model.sale;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductReport;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

@Getter @Setter @NoArgsConstructor
public class SalableProductReport extends AbstractIdentifiableReport<SalableProductReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private ProductReport product = new ProductReport();
	private String price;

	@Override
	public void setSource(Object source) {
		super.setSource(source);
		product.setSource(((SalableProduct)source).getProduct());
		price = format(((SalableProduct)source).getProperties().getPrice());
	}
	
	@Override
	public void generate() {
		super.generate();
		product.generate();
		price = provider.randomInt(1, 1000)+"";
	}
}
