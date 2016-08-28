package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.root.business.impl.AbstractCollectionDetails;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductCollectionDetails extends AbstractCollectionDetails<SalableProductCollection> implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	
	public SalableProductCollectionDetails(SalableProductCollection salableProductCollection) {
		super(salableProductCollection);
		
	}
	
	/**/
	
	
}