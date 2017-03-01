package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;
import org.cyk.system.root.model.party.person.ActorReport;

@Getter @Setter @NoArgsConstructor
public class AbstractSaleReport<MODEL> extends AbstractIdentifiableReport<MODEL> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	protected ActorReport customer = new ActorReport();
	protected SalableProductCollectionReport salableProductCollection = new SalableProductCollectionReport();
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		customer.setSource(((Sale)source).getCustomer());
		//salableProductCollection.setSale(this);
		salableProductCollection.setSource(((Sale)source).getSalableProductCollection());
		
	}
	
	@Override
	public void generate() {
		super.generate();
		customer.generate();
		//salableProductCollection.setSale(this);
		salableProductCollection.generate();
	}
	
	
	
}
