package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.ProductCollectionItem;
import org.cyk.system.root.business.impl.AbstractOutputDetails;

public class ProductCollectionItemDetails extends AbstractOutputDetails<ProductCollectionItem> implements Serializable {

	private static final long serialVersionUID = 2521346504962821065L;

	public ProductCollectionItemDetails(ProductCollectionItem master) {
		super(master);
	}

}
